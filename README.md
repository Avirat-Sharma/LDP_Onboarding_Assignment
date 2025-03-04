# Initial Docker commands:
- docker compose -f "C:\Users\avirat.sharma\OneDrive - ION\Desktop\Work\LDP-Practice\LDP_Onboarding_Assignment\COOL_Onboarding_Activity\docker.compose.yml" up -d
- docker ps

# Producer command:
- docker exec -it kafka kafka-console-consumer --topic bond-prices --bootstrap-server localhost:9092 --from-beginning

# Consumer command:
- docker exec -it kafka kafka-console-consumer --topic enhanced-bond-prices --bootstrap-server localhost:9092 --from-beginning


# Commands for deleting messages for fresh testing:

- docker exec -it kafka kafka-topics --delete --topic bond-prices --bootstrap-server localhost:9092
- docker exec -it kafka kafka-topics --delete --topic enhanced-bond-prices --bootstrap-server localhost:9092
- docker exec -it kafka kafka-topics --create --topic bond-prices --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
- docker exec -it kafka kafka-topics --create --topic enhanced-bond-prices --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
