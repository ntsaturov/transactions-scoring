# transactions-scoring
Transaction scoring system with supporting of lua-rules scripts.

[Public backlog](https://trello.com/b/zdg7RtHf/transactions-scoring-system)

<img src="https://github.com/ntsaturov/transactions-scoring/blob/feature/dev-build/docs/schema.png" width="800" height="650">

#### ADT description

1. id - operation id - UUID
2. transactionId - transaction id - number like a string, size - 12 characters
3. rules - list of rules tags
4. status - transaction status - HOLD -> OK / BLOCK
5. comment - field with supporting information for analysts;
6. data - transaction data block of custom fields:

   - SOURCE_PHONE - source phone number
   - SOURCE_ID -  source payment-account id
   - AMOUNT
   - DATE  - transaction date
   - IP - client ip
   - RECIPIENT_PHONE - recipient phone number
   - RECIPIENT_ID - recipient payment-account id
   - PAYMENT_COMMENT - payment comment

Transaction example:

```
{
    "id": "00dd07a1-3877-4657-a840-9b72068b1565",
    "transactionId": "111122224444"
    "rules": ["rule_name_1", "rule_name_2", "rule_name_3"],
    "status": "HOLD",
    "comment": "",
    "data": {
        "SOURCE_PHONE": "+79899533321",
        "SOURCE_ID": "123456789",
        "AMOUNT": "190.00",
        "DATE": "2023-03-28 14:53:43",
        "IP": "172.168.111.112",
        "RECIPIENT_PHONE": "79899654433",
        "RECIPIENT_ID": "345678789",
        "PAYMENT_COMMENT": "money loan"
    }
}
```


#### Purpose

#### Environment variables

#### Metrics

#### Installation

#### Build a jar and run it

#### Build source jar

#### Run project

