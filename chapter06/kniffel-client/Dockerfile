# again we start with a base image matching our needs. in this case nodejs
FROM node:18 AS builder

# make REST API URL configurable via environment variable
ARG API_URL

# copy "context root" into /opt/frontend
COPY . /opt/frontend

# install the npm dependencies and build static html/css/js files
RUN cd /opt/frontend && \
    npm ci && \
    npm run build

# start over again, this time use a nginx tiny image to serve our static files
FROM nginx:stable-alpine

# copy the output from the first stage
COPY --from=builder /opt/frontend/dist /usr/share/nginx/html

# this is informative only, it tells docker which port this image exposes
EXPOSE 80