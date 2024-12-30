Q: I still don't understand how spark can filter without deserialization  ...  
I say age > 25 ... and spark has a binary representation of the age data ( so it doesn't see any values there) 
Can you explain how this works?

A: You're absolutely right to question this. The idea that Spark can filter without deserialization requires clarification of what "deserialization" means in this context and how Spark processes data stored in its binary format.

## How Spark Filters Without Full Deserialization
When we say Spark doesn't "deserialize" during SQL-like operations (e.g., filter("age > 25")), 
it means Spark does not deserialize the data into full JVM objects (like Scala case classes or rows). Instead, Spark directly processes the serialized binary data, but it still "reads" the values (like age) from this binary format.
Here’s how it works step-by-step:

Step-by-Step Explanation
1. Binary Format (Tungsten's Columnar Storage)

   In Spark’s optimized Tungsten engine, DataFrame/Dataset data is stored in a compact binary format, often in a columnar representation.
   Each column (e.g., age) is stored separately and contiguously in memory.
   For primitive data types (e.g., integers, floats), the binary format directly stores their values in a compact, fixed-width representation (e.g., 4 bytes for int).

2. Accessing Values Without Full Deserialization

   When Spark executes a SQL-like filter, it doesn't need to deserialize the entire row (or object). Instead:
   Columnar Access: Spark retrieves only the age column from the serialized data. This is efficient because the columnar format enables Spark to access specific fields directly.
   Binary Read: Spark interprets the binary representation of age (e.g., 4 bytes for an integer) and evaluates the filter condition (age > 25) directly.
   Skip Object Construction: Spark skips reconstructing the full JVM object for the row unless explicitly required (e.g., by user code).

3. Predicate Pushdown

In some cases, Spark pushes filters (age > 25) down to the storage layer (e.g., Parquet, ORC). These formats also store data in a columnar layout, allowing Spark to filter directly at the data source without even loading irrelevant rows.