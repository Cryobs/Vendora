# Todo list
- ~~price service~~
  - ~~discount system~~
- order service
  - create order
    1. ~~get data~~
    2. ~~calculate price~~
    3. ~~use promo~~
    4. ~~reserve products~~
    5. payment
    6. change order status
  - check order
  - update order
- payment service
  - payment simulation
  - link to order
- auth-user service
  - ~~Keycloak~~


# Docker commands
## Build Keycloak Dockerfile
```bash
    sudo docker build -t keycloak-custom -f keycloak-config/Dockerfile.keycloak .
```

## Run Keycloak 
```bash
    sudo docker run -p 8090:8080 -v keycloak_data:/opt/keycloak/data -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin keycloak-custom
```

## Pull Elastic Images
```bash
    sudo docker pull docker.elastic.co/elasticsearch/elasticsearch:8.17.2
    sudo docker pulldocker.elastic.co/kibana/kibana:8.17.2
```

## Run ElasticSearch
```bash
  cd elasticsearch-config
  sudo docker-compose up -d 
```

## Run PostgreSQL
```bash
sudo docker run --name postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=Vendora -d -p 5432:5432 postgres
```

## Run docker compose
```bash
sudo docker-compose up -d 
```
