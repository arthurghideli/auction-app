# Project description
This is an open-scope project to address a single, lowest-bid issue in an auction. For study and evaluation purposes.

## Prerequisites
- JDK 11
- Lombok
- Spring boot

## USAGE
## Open a auction to begin.

post -> http://localhost:8080/auction-app/auction/open

If a auction is already opened the message "Auction is already open!" will be shown (http 200). Any other case (http 500)

## Make a bid

post -> http://localhost:8080/auction-app/auction/sendBid

If there is no auction opened the message "There is no active auction to receive bid" will be shown (http 200). Any other case (http 500)

payloads:
{
    "ownerName": "Arthur",
    "bid": 0.01
}

{
    "ownerName": "Pedro",
    "bid": 12.34
}

{
    "ownerName": "Maria",
    "bid": 0.3
}
{
    "ownerName": "Jessica",
    "bid": 0.01
}

## Close auction 

post -> http://localhost:8080/auction-app/auction/close

if is already closed the message "There is no active auction to be closed!" will be shown (http 200). Any other case (http 500)

Output for this exemple:
{
    "bid": {
        "ownerName": "Maria",
        "bid": 0.3
    },
    "totalCollected": 3.92,
    "output": "Vencedor: Maria , com lance de 0.3 e arrecadação de 3.92"
}
