const http = require("http");
const url = require("url");
const core = require("./core");

const PIN = process.env.PIN;
const PORT = process.env.PORT || 3001;

if (!PIN) {
  console.error("Error: PIN environment variable is not set.");
  process.exit(1);
}

const routes = {
  GET: {},
  POST: {},
  DELETE: {},
};

const get = (path, handler) => (routes.GET[path] = handler);
const post = (path, handler) => (routes.POST[path] = handler);
const del = (path, handler) => (routes.DELETE[path] = handler);

// Declarative Route Definitions
get("/get", (req, res, query) => {
  const result = core.getPassword(query.name || null);
  return { result };
});

get("/pin/len", (req, res, query) => {
  return { result: PIN.length };
});

get("/pin/verify", (req, res, query) => {
  return { result: query.pin === PIN };
});

get("/vault", () => ({ result: core.getVault() }));
get("/gen", () => ({ result: core.generatePassword() }));
get("/key/path", () => ({ result: core.getKeyPath() }));

post("/add", async (req) => {
  const data = await getBody(req);
  if (!data.name || !data.password) throw new Error("Missing name or password");
  core.addPassword(data.name, data.password);
  return { result: "Password added" };
});

post("/cfg", async (req) => {
  const data = await getBody(req);
  if (!data.path) throw new Error("Missing path");
  core.cfgVault(data.path);
  return { result: "Vault configured" };
});

post("/key/gen", () => {
  core.generateKey();
  return { result: "Key generated" };
});

del("/del", (req, res, query) => {
  if (!query.name) throw new Error("Missing name parameter");
  core.delPassword(query.name);
  return { result: "Password deleted" };
});

// Helper to parse JSON body
const getBody = (req) =>
  new Promise((resolve, reject) => {
    let body = "";
    req.on("data", (chunk) => (body += chunk.toString()));
    req.on("end", () => {
      try {
        resolve(body ? JSON.parse(body) : {});
      } catch (e) {
        reject(new Error("Invalid JSON body"));
      }
    });
  });

const server = http.createServer(async (req, res) => {
  const parsedUrl = url.parse(req.url, true);
  const { pathname, query } = parsedUrl;
  const method = req.method;

  // CORS headers
  res.setHeader("Access-Control-Allow-Origin", "*");
  res.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
  res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

  if (method === "OPTIONS") {
    res.writeHead(204);
    return res.end();
  }

  // Authorization check
  if (req.headers.authorization !== PIN && !pathname?.includes("pin")) {
    res.writeHead(401, { "Content-Type": "application/json" });
    return res.end(JSON.stringify({ error: "Unauthorized" }));
  }

  const handler = routes[method] && routes[method][pathname];

  if (handler) {
    try {
      const response = await handler(req, res, query);
      res.writeHead(200, { "Content-Type": "application/json" });
      res.end(JSON.stringify(response));
    } catch (error) {
      res.writeHead(error.message.includes("Missing") ? 400 : 500, {
        "Content-Type": "application/json",
      });
      res.end(JSON.stringify({ error: error.message }));
    }
  } else {
    res.writeHead(404, { "Content-Type": "application/json" });
    res.end(JSON.stringify({ error: "Not Found" }));
  }
});

server.listen(PORT, () => {
  console.log(`Server running at http://localhost:${PORT}/`);
});
