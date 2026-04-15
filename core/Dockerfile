FROM node:20-alpine

WORKDIR /app

COPY . .

ENV NODE_ENV=production
ENV PORT=3001
ENV PSMG_CONFIG_STORAGE=/appconfig

RUN mkdir -p /appconfig
RUN mkdir -p /vault
VOLUME /appconfig
VOLUME /vault

EXPOSE 3001

CMD ["node", "core/server.js"]
