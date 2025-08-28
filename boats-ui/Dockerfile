FROM node:22-bookworm

WORKDIR /app
EXPOSE 4200

ENV CHOKIDAR_USEPOLLING=true \
    NG_CLI_ANALYTICS=false \
    HOST=0.0.0.0

CMD ["/bin/sh","-lc","[ -f package-lock.json ] && npm ci || npm install; npm run start -- --host 0.0.0.0 --port 4200 --proxy-config docker.proxy.conf.json"]
