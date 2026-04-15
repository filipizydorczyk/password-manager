#!/bin/node
const readline = require("readline");
const core = require("./core");

const args = process.argv.slice(2);
const command = args[0];

const askPassword = () => {
  const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout,
  });

  return new Promise((resolve) =>
    rl.question("Password: ", (ans) => {
      rl.close();
      resolve(ans);
    })
  );
};

const main = async () => {
  try {
    if (command === "add" && args.length === 3) {
      const [, name, password] = args;
      core.addPassword(name, password);
      process.exit(0);
    }
    if (command === "add" && args.length === 2) {
      const [, name] = args;
      const pass = await askPassword();
      core.addPassword(name, pass);
      process.exit(0);
    }
    if (command === "del" && args.length === 2) {
      const [, name] = args;
      core.delPassword(name);
      process.exit(0);
    }
    if (command === "get" && (args.length === 2 || args.length === 1)) {
      const [, name] = args;
      const result = core.getPassword(name);
      if (Array.isArray(result)) {
        console.log(result.join("   "));
      } else {
        console.log(result);
      }
      process.exit(0);
    }
    if (command === "key" && args.length === 2) {
      const [, action] = args;
      if (action === "gen") core.generateKey();
      if (action === "get") console.log(core.getKeyPath());
      process.exit(0);
    }
    if (command === "cfg" && args.length === 2) {
      const [, path] = args;
      core.cfgVault(path);
      process.exit(0);
    }
    if (command === "gen") {
      console.log(core.generatePassword());
      process.exit(0);
    }
    if (command === "vault") {
      console.log(core.getVault());
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
    
    // Default behaviors matching api.js
    if (args.length === 0) {
      const result = core.getPassword();
      console.log(result.join("   "));
      process.exit(0);
    }
    if (args.length === 1) {
      console.log(core.getPassword(args[0]));
      process.exit(0);
    }

    console.log("No action performed. Try help command");
    process.exit(127);
  } catch (e) {
    console.error(e.message);
    process.exit(1);
  }
};

main();
