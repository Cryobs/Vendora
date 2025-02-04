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
    docker build -t keycloak-custom -f keycloak-config\Dockerfile.keycloak .
```

## Run Keycloak 
```bash
docker run -p 8090:8080 -v keycloak_data:/opt/keycloak/data -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin keycloak-custom
```

