FROM node:22-alpine
WORKDIR /app
COPY .output .output
EXPOSE 3000
ENV HOST=0.0.0.0
ENV PORT=3000
ENTRYPOINT ["node", ".output/server/index.mjs"]
