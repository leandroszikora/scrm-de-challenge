## Solution
In this case, it occured to me that one solution could be to use distributed programming tools, like Spark, 
because it would allow to load the dataset to work (value list) as a RDD or DataFrame, 
and work it with transformations that will be executed (lazy) in each partition chosen by Spark.

An alternative would be to work with intermediate results that are persisted in disk, but the problem is that the solution becomes too complex, because the writing in disk functionality is aggregated, but it is also necessary to combine each part. For those reasons, it would be necessary to:
1. Order the value list in parts, i.e. processing and persisting in disk.
2. Generate the parts.
3. Filter the results that are not possible.
4. Calculate a result for each one and make a combined calculation between each one of the parts.
