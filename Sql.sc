import scala.collection.JavaConverters._

val csvData = spark.sparkContext.parallelize(
  """
    |ID,FIRST_NAME,LAST_NAME,DESIGNATION,DEPARTMENT,SALARY
    |1001,Ram,Ghadiyaram,Director of Sales,Sales,30000
    |1002,Ravi,Rangasamy,Marketing Manager,Sales,25000
    |1003,Ramesh, Rangasamy,Assistant Manager,Sales,25000
    |1004,Prem,Sure,Account Coordinator,Account,15000
    |1005,Phani ,G,Accountant II,Account,20000
    |1006,Krishna,G,Account Coordinator,Account,15000
    |1007,Rakesh,Krishnamurthy,Assistant Manager,Sales,25000
    |1008,Gally,Johnson,Manager,Account,28000
    |1009,Richard,Grill,Account Coordinator,Account,12000
    |1010,Sofia,Ketty,Sales Coordinator,Sales,20000
    |""".stripMargin.lines.toList.asScala).toDS()

val frame = spark.read.option("header", true).option("inferSchema", true).csv(csvData)
frame.show
frame.shema
frame.createOrReplaceTempView("emp_dept_tbl")

//select the 5th largest salary
spark.sql(
  """
    |select * from (
    | select first_name, last_name, department, salary, dense_rank() over(order by salary desc) as rank from emp_dept
    |) where rank = 5
    |""".stripMargin).show
