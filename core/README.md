```sh
docker build -t password-manager-server -f core/Dockerfile .
docker run -p 3001:3001 -e PIN=your_secret_pin password-manager-server
```
