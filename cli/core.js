const crypto = require("crypto");
const fs = require("fs");
const path = require("path");

const algorithm = "aes-256-cbc";
const configstorage = path.join(process.env.HOME, ".fpasscfg");
const configfile = path.join(configstorage, "cfg.json");
const keyfile = path.join(configstorage, "key");

// Helper to ensure config directory exists
if (!fs.existsSync(configstorage)) {
  fs.mkdirSync(configstorage, { recursive: true });
}

const getConfig = () => {
  if (!fs.existsSync(configfile)) return {};
  try {
    return JSON.parse(fs.readFileSync(configfile, "utf8"));
  } catch (e) {
    return {};
  }
};

const cfgVault = (vaultPath) => {
  if (!vaultPath) {
    throw new Error("You need to specify path for your vault!");
  }

  if (!fs.existsSync(vaultPath)) {
    throw new Error("Vault needs to be valid path!");
  }

  const cfg = JSON.stringify({ vault: vaultPath });
  fs.writeFileSync(configfile, cfg);
};

const addPassword = (name, password) => {
  const config = getConfig();
  if (!config.vault) {
    throw new Error("Please configure vault first with cfg command");
  }

  const filepath = path.join(config.vault, `${name}.json`);

  if (fs.existsSync(filepath)) {
    throw new Error("Password already exists!");
  }

  if (!fs.existsSync(keyfile)) {
    throw new Error("Encryption key not found. Generate one with 'key gen'");
  }

  const iv = crypto.randomBytes(16);
  const key = fs.readFileSync(keyfile);

  const cipher = crypto.createCipheriv(algorithm, Buffer.from(key), iv);
  let encrypted = cipher.update(password, "utf8", "hex");
  encrypted += cipher.final("hex");

  const response = { iv: iv.toString("hex"), encryptedData: encrypted };
  fs.writeFileSync(filepath, JSON.stringify(response));
};

const getPasswords = () => {
  const config = getConfig();
  if (!config.vault) {
    throw new Error("Please configure vault first with cfg command");
  }
  const files = fs.readdirSync(config.vault);
  return files
    .filter((file) => file.endsWith(".json"))
    .map((file) => file.replace(".json", ""));
};

const getPassword = (name) => {
  const config = getConfig();
  if (!config.vault) {
    throw new Error("Please configure vault first with cfg command");
  }

  if (!name) {
    return getPasswords();
  }

  if (!fs.existsSync(keyfile)) {
    throw new Error("Encryption key not found.");
  }

  const key = fs.readFileSync(keyfile);
  const passfile = path.join(config.vault, `${name}.json`);
  
  if (!fs.existsSync(passfile)) {
    throw new Error("Password doesn't exist");
  }

  const passobj = JSON.parse(fs.readFileSync(passfile, "utf8"));
  if (!passobj.iv || !passobj.encryptedData) {
    throw new Error("Password file is corrupted");
  }

  const decipher = crypto.createDecipheriv(
    algorithm,
    Buffer.from(key),
    Buffer.from(passobj.iv, "hex")
  );
  let decrypted = decipher.update(passobj.encryptedData, "hex", "utf8");
  decrypted += decipher.final("utf8");
  return decrypted;
};

const delPassword = (name) => {
  const config = getConfig();
  if (!config.vault) {
    throw new Error("Please configure vault first with cfg command");
  }

  if (!name) {
    throw new Error("Please specify password to remove");
  }

  const passfile = path.join(config.vault, `${name}.json`);
  if (!fs.existsSync(passfile)) {
    throw new Error("Password doesn't exist");
  }

  fs.unlinkSync(passfile);
};

const generateKey = () => {
  const keycontent = crypto.randomBytes(32);

  if (fs.existsSync(keyfile)) {
    throw new Error("Key already exists.");
  }

  fs.writeFileSync(keyfile, keycontent);
};

const getVault = () => getConfig().vault;

const generatePassword = () => crypto.randomBytes(12).toString("hex");

const getKeyPath = () => keyfile;

module.exports = {
  cfgVault,
  addPassword,
  getPasswords,
  getPassword,
  delPassword,
  generateKey,
  getVault,
  generatePassword,
  getKeyPath
};
