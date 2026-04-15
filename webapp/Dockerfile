FROM node:20-alpine

RUN npm install -g pnpm

WORKDIR /app

COPY . .

WORKDIR /app/webapp

RUN pnpm install --frozen-lockfile

RUN pnpm run build

ENV NODE_ENV=production
ENV PORT=3000

EXPOSE 3000

CMD ["node", ".output/server/index.mjs"]
