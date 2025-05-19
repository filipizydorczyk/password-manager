This password manager was created for personal use and I don't recommend to use it. It uses AES 256 as encryption algorythm and provides cli and anrdoid application.

<div style="display: flex">
    <img src="./_screenshots/Screenshot%20from%202025-05-19%2018-45-22.png" alt="Screenshot from 2025-05-19 18-45-22" width="200px" height="auto">
    <img src="./_screenshots/Screenshot%20from%202025-05-19%2018-46-04.png" alt="Screenshot from 2025-05-19 18-46-04" width="200px" height="auto">
    <img src="./_screenshots/Screenshot%20from%202025-05-19%2018-46-17.png" alt="Screenshot from 2025-05-19 18-46-17" width="200px" height="auto">
    <img src="./_screenshots/Screenshot%20from%202025-05-19%2018-46-29.png" alt="Screenshot from 2025-05-19 18-46-29" width="200px" height="auto">
    <img src="./_screenshots/Screenshot%20from%202025-05-19%2018-46-47.png" alt="Screenshot from 2025-05-19 18-46-47" width="200px" height="auto">
    <img src="./_screenshots/Screenshot%20from%202025-05-19%2018-46-57.png" alt="Screenshot from 2025-05-19 18-46-57" width="200px" height="auto">
    <img src="./_screenshots/Screenshot%20from%202025-05-19%2018-47-06.png" alt="Screenshot from 2025-05-19 18-47-06" width="200px" height="auto">
    <img src="./_screenshots/Screenshot%20from%202025-05-19%2018-47-18.png" alt="Screenshot from 2025-05-19 18-47-18" width="200px" height="auto">
</div>

```sh
 ➜ cli (master) ✏️  node ./api.js help

        Available commands:
          app add <name> <password>   - Add a new password
          app del <name>              - Delete a password
          app key gen                 - Generate a random key
          app key get                 - Get the generated key (path)
          app get <name>              - Get password (if empty it lists them)
          app cfg <path>              - Sets vault path
          app gen                     - Generate password
          app help                    - Show this help message
```

Both apps can work in parralel if vaults are synced by external software and use the same key file.
