# Vicarius Backend Interview Project

## Information
For the purpose of demonstration, an in-memory cache system was utilized. However, for a solution that is more reliable 
and scalable, it is recommended to use Redis, which can seamlessly replace the in-memory cache within the QuotaLimiter service.

As instructions were not clear regarding the time for refreshing quotes, a configurable solution was implemented. 
The default configuration allows for 5 requests per day.

The database used was H2 for the sake of convenience simulating MySql. Elastic was emulated. 
However, the ElasticUserRepository was designed with flexibility, allowing easy modification to point to a real Elastic endpoint.

The implementation of the access-limiting mechanism was based on the token bucket strategy.
