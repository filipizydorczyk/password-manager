#!/bin/node
const crypto = require("crypto");
const fs = require("fs");
const path = require("path");

const algorithm = "aes-256-cbc";
const configstorage = path.join(process.env.HOME, ".fpasscfg");
const configfile = path.join(configstorage, "cfg.json");
const keyfile = path.join(configstorage, "key");

const config = JSON.parse(fs.readFileSync(configfile) || {});

const args = process.argv.slice(2);
const command = args[0];

const cfgVault = (vault) => {
  if (!vault) {
    console.log("You need to specify path for your vault!");
    process.exit(0);
  }

  if (!fs.existsSync(vault)) {
    console.log("Vault needs to be valid path!");
    process.exit(0);
  }

  const cfg = JSON.stringify({ vault });
  fs.writeFileSync(configfile, cfg);
};

const addPassword = (name, password) => {
  // adding dirctories?
  if (!config.vault) {
    console.log("Please configure vault first with cfg command");
    process.exit(0);
  }

  const filepath = path.join(config.vault, `${name}.json`);

  if (fs.existsSync(filepath)) {
    console.log("Password already exists!");
    process.exit(0);
  }

  const iv = crypto.randomBytes(16);
  const key = fs.readFileSync(keyfile);

  const cipher = crypto.createCipheriv(algorithm, Buffer.from(key), iv);
  let encrypted = cipher.update(password, "utf8", "hex");
  encrypted += cipher.final("hex");

  const response = { iv: iv.toString("hex"), encryptedData: encrypted };
  fs.writeFileSync(filepath, JSON.stringify(response));
};

const getPassword = (name) => {
  if (!config.vault) {
    console.log("Please configure vault first with cfg command");
    process.exit(0);
  }

  if (!name) {
    const files = fs.readdirSync(config.vault);
    console.log(
      files
        .filter((file) => file.includes(".json"))
        .map((file) => file.replace(".json", ""))
        .join("   ")
    );
    process.exit(0);
  }

  const key = fs.readFileSync(keyfile);

  const passfile = path.join(config.vault, `${name}.json`);
  if (!fs.existsSync(passfile)) {
    console.log("Password doesn't exist");
    process.exit(0);
  }

  const passobj = JSON.parse(fs.readFileSync(passfile, "utf8"));
  if (!passobj.iv || !passobj.encryptedData) {
    console.log("Password file is corrupted");
    process.exit(1);
  }

  const decipher = crypto.createDecipheriv(
    algorithm,
    Buffer.from(key),
    Buffer.from(passobj.iv, "hex")
  );
  let decrypted = decipher.update(passobj.encryptedData, "hex", "utf8");
  decrypted += decipher.final("utf8");
  console.log(decrypted);
};
const delPassword = (name) => {
  if (!config.vault) {
    console.log("Please configure vault first with cfg command");
    process.exit(0);
  }

  if (!name) {
    console.log("Please specify password to remove");
    process.exit(0);
  }

  const passfile = path.join(config.vault, `${name}.json`);
  if (!fs.existsSync(passfile)) {
    console.log("Password doesn't exist");
    process.exit(0);
  }

  fs.unlinkSync(passfile);
};
const generateKey = () => {
  const keycontent = crypto.randomBytes(32);

  if (fs.existsSync(keyfile)) {
    console.log("Key already exists.");
    process.exit(0);
  }

  fs.writeFileSync(keyfile, keycontent);
};

if (command === "add" && args.length === 3) {
  const [cmd, name, password] = args;
  addPassword(name, password);
  process.exit(0);
}
if (command === "del" && args.length === 2) {
  const [cmd, name] = args;
  delPassword(name);
  process.exit(0);
}
if (command === "get" && (args.length === 2 || args.length === 1)) {
  const [cmd, name] = args;
  getPassword(name);
  process.exit(0);
}
if (command === "key" && args.length === 2) {
  const [cmd, action] = args;
  if (action === "gen") generateKey();
  if (action === "get") console.log(keyfile);
  process.exit(0);
}
if (command === "cfg" && args.length === 2) {
  const [cmd, path] = args;
  cfgVault(path);
  process.exit(0);
}
if (command === "gen") {
  console.log(crypto.randomBytes(12).toString("hex"));
  process.exit(0);
}
if (command === "help") {
  console.log(`
        Available commands:
          app add <name> <password>   - Add a new password
          app del <name>              - Delete a password
          app key gen                 - Generate a random key
          app key get                 - Get the generated key (path)
          app get <name>              - Get password (if empty it lists them)
          app cfg <path>              - Sets vault path
          app gen                     - Generate password
          app help                    - Show this help message
        `);
  process.exit(0);
}
if (args.length === 0) {
  getPassword();
  process.exit(0);
}
if (args.length === 1) {
  getPassword(args[0]);
  process.exit(0);
}

console.log("No action performed. Try help command");
process.exit(127);
