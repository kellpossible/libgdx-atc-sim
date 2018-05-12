# Coding Standards

## Naming conventions

#### General
+ No Magic numbers. All variable names must be meaningful. A possible exception is for mathematical equations, where magic numbers can’t be avoided. THe use of magic numbers during Unit Testing is permitted, but expressive comments are required to explain use.
+ Only abbreviate if the name is getting too long (more than 15 characters).
+ Use mixed case [(Camel Case)](https://en.wikipedia.org/wiki/CamelCase) to make names more readable.
+ Avoid underscores (except with static final) and $.
+ Don’t redefine standard identifiers.
+ Don’t use magic numbers where it can be avoided. (Maths equations)

#### Redundant Prefixes and Suffixes

Avoid wherever possible redundant prefixes and suffixes. No [hungarian notation](https://en.wikipedia.org/wiki/Hungarian_notation).  This project uses
intellij which provides most of this information just by holding CTRL and
hovering your mouse over it.

For example a method with redundant prefixes on variable names which
make the api and internal code less consistent, and names harder to read:

```java
public class SomeClass
{
  private int variable;
  private int someOtherVariable;

  /**
  * Constructor for SomeClass
  * @param aVariable1 some variable
  * @param aSomeOtherVariable2 some other variable
  */
  public SomeClass(int aVariable1, int aSomeOtherVariable2)
  {
    variable = aVariable1;
    someOtherVariable = aSomeOtherVariable2;
  }
}
```


A better way to do it, which makes use of scoping:
```java
public class SomeClass
{
  private int variable;
  private int someOtherVariable;

  /**
  * Constructor for SomeClass
  * @param variable some variable
  * @param someOtherVariable some other variable
  */
  public SomeClass(int variable, int someOtherVariable)
  {
    this.variable = variable;
    this.someOtherVariable = someOtherVariable;
  }
}
```


#### Classes
+ Use a descriptive name with the first letter of each word including the first word in upper case e.g. HomeGroup.


#### Objects
+ Use a descriptive name; do not capitalize the first letter of the name (which is reserved for naming classes as described in 1.2)


#### Instance Variables
+ Use a descriptive name for the instance variable with the first letter in lower case but with the first letter of any subsequent words in upper case e.g. theYearGroup, tv.


#### Local Variables
+ Use a description of the variable with the first letter in lower case but with the first letter of any subsequent words in upper case. Do not use the same name as any existing instance variables.


#### Final Static Instance Variables
+ For primitive constants such as int, use all upper case letters with underscores separating the words e.g. MAX_CLASS_SIZE.
+ For object constants, use the normal instance variable rules e.g. Color.white


## Methods


#### ‘Get’ methods
+ Prefix the name of the instance variable being accessed, with ‘get’ unless the method is a boolean, in which case prefix the name with ‘is’ e.g. getDetails() or isFull()


#### ‘Set’ methods
+ Prefix the name of the instance variable being accessed, with ‘set’ e.g. setName()


#### Other methods
+ Use a description of what the method does. Start with a verb if possible, with the first letter in lower case e.g. readMark() or addStudent().


## Comments


#### General
+ Keep comments precise and concise. Comments should add to the clarity of your code. Do not state the obvious.
+ See Oracle's javadoc style guide for a more detailed guide on using javadoc
  code comments [here](http://www.oracle.com/technetwork/java/javase/documentation/index-137868.html).


#### Header Comments
+ The first line of any header comment starts with a javadoc \/\*\* and each
subsequent line has an (optional) asterisk in the second column. The comment
block is terminated with \*\/


#### Class headers
+ Each class starts with a comment that documents concisely the basic information
  a user will need to write code that uses the class.

Example:

```java
/**    
* A concise description of the class, and key concepts related to the use
* of the class which are not immediately obvious when reading the rest of
* the javadoc associated with the class's variable and methods.
*
* @author author1
* @author author2
*/
```


#### Method headers
+ Unless obvious, methods must have a comment at their heads.
+ The method comments should provide most of the contract e.g. the pre-condition and the post-condition plus what is returned (preceded by an @return) and parameter descriptions (preceded by an @param) if needed. There should be one@param per method parameter. Since methods can only have a single return value, there should only ever be a single @return statement, if required.

Example (with leading asterisks on each line):

```java
/**
* Print name and average mark of every student in the group
* Precondition: students have valid names and marks
* @param wantDetails true: show all marks
*/
public void printReport(boolean wantDetails)
{
//…
}
```

Example (without leading asterisks on each line):

```java
/**
    Print name and average mark of every student in the group
    Precondition: students have valid names and marks
    @param wantDetails true: show all marks
*/
public void printReport(boolean wantDetails)
{
//…
}
```


Example of method with no return and two parameters:


```java
/**
   Print the sum of the two parameters.
   @param valueOne
   @param valueTwo
 */
public void printSum(int valueOne, int valueTwo)
{
    System.out.println(valueOne + valueTwo);
}
```

Example of a method with one parameter and a return:

```java
/**
   Returns half the value of the parameter.
   @param value
   @return half of the parameter.
 */
public double getHalf(double value)
{
    return value/2;
}
```


#### Single line comments
+ Short comments can appear on a single line indented to the level of the code using the "\/\/" documentation symbol rather than the "\/\*".


Example:

```java
// Now simulate 10 minutes
System.out.println("Now we run the timer for 10 minutes…");
```

#### Trailing comments:
+ Very short comments can appear on the same line as the code that they describe.

Example:

```java
theTimer.setVisible(true);    // make its value visible
```


## Declarations


#### Variables
+ There should be only one declaration per line with trailing comments only if necessary.
Instance Variables
+ should be declared at the start of the class definition.
Local Variables
+ can be declared either as required or at the start of a block.
+ When declaring arrays, the declaration form is type [] arrayname; not the C format.


#### Methods
+ Constructors and/or the init() method are declared first, followed by the ‘get’, ‘set’ and other methods in alphabetical order.


#### Empty Method Bodies
+ These so-called "null" bodies are written with both braces on the same line.


Example:

```java
public void mouseClicked(MouseEvent e)
{}
public void mouseEntered(MouseEvent e)
{}
public void mouseExited(MouseEvent e)
{}
public void mouseMoved(MouseEvent e)
{}
```


## Control Structures


#### General
+ The use of braces is required for almost all control structures. The exception is a single statement in an ‘if’ statement. The braces always appear on a line by themselves and are always aligned with the beginning of the keyword.
+ The code within the braces is always indented. The indentation quantum should be the same throughout the set of software, preferably 4 spaces, no less than 2.
+ Avoid using ‘continue’ and ‘break’ in loops.
+ Always use a default in switch statements. Use a break to end all cases in a switch statement.


while:

```java
while (condition)
{
    statements;
}
```

for:

```java
for (int i = initial; i < max; i++)
{
    statements;
}
```

do:

```java
do
{
    statements;
}
while (condition);
```

if:
+ The if statement does not require braces where there is a single statement.

```java
if (condition)
    statement;

//or

if (condition)
{
    statements;
}
if (condition)
    statement;
else
    statement;

//or

if (condition)
{
    statements;
}
else
{
    statements;
}

if(condition)
    statement;
else if(condition)
    statement;
else
    statement;

//or

if (condition)
{
    statements;
}
else if (condition)
{
    statements;
}
else
{
    statements;
}
```

switch:

```java
switch (int_expression)
{
    case int_value_1:
         statement_1;
         statement_2;
       break;
    case int_value_2:
        statements;
       break;
    //...
    default:
       statements;
}
```

exception handling:

```java
try
{
    statements;
}
catch (exception_type name)
{
    statements;
}
```

+ If the catch does nothing, treat it like an empty method body, i.e. {}



## Statement Layout

#### Operators
+ The operators (+ - =) and the relational operators (== != etc) require a space on either side. The multiplicative operators (* / %) usually have a space on either side. There are circumstances when, to improve readability, some of these spaces are omitted.
Continuation
+ When a statement that is already indented exceeds 80 characters, the statement should be split over more than one line and the continuation lines should be indented by more than the indentation quantum.
+ The split should be before or after an operator. Preferably align similar parts of expressions under one another.

Examples:

```java
aName = Keyboard.readString("Enter the name for student " +
    (numberOfStudents + 1) + ": ");      // counts from 1

aName = Keyboard.readString("Enter the name for student "
     + (numberOfStudents + 1) + ": ");      // counts from 1

return groupName + ", " + roomNumber + ", " + teacherName +
            ", " + numberOfStudents + " students";

return groupName + ", " + roomNumber + ", " + teacherName
          + ", " + numberOfStudents + " students";

theGroup = new HomeGroup("12K", "ES304", "K Gilgamesh",
          numberOfStudents);
```


## Forbidden
+ The following are forbidden.
+ ++, -- and = within expressions, and goto

Example:

```java
int x = 10;

if(x++ > 10)//incorrect
{
    //statments
}

if(x > 10)//correct
{
    //statments
}
x++;
```


## Visibility
+ Features of classes should have specified visibility (public/private/protected) as appropriate. Instance variables should be private (or sometimes protected).
+ A class is marked as public only when necessary i.e. 1) applet 2) a visible class of a package (meant to be used from outside the package)

Examples:

```java
/**
 *  Branch is a finite collection of accounts.  Accounts may be retrieved by
 *  position (index in array).  Also supports query of the account with
 *  maximum or minimum balance.
 *
 * @author Annette Oppenheim
 * @author Rob Allen
 * @author Mike Creek, Swinburne University of Technology
 */

class Branch
{
    private int maxItems; //maximum number of items in the branch
    private Account [] branchAccount; //account of the branch
    private int branchItems; //number of items in the branch
    private String name; //name of the branch

    /**
    * Constructor for Branch class.
    * @param name the name of the branch
    * @param maxItems the maximum number of items in the branch
    */
    public Branch(String name, int maxItems)
    {
        this.name = name;
        this.maxItems = maxAccounts;
        branchAccount = new Account[maxItems];
        branchItems = 0;
    }

    /**
     * If room, creates an account and adds it to the collection, else
     * prints an error.
     * @param customerName the customers name as a string
     * @param initDeposit the initial deposit as a double
     */
    public void addAccount(String customerName, double initDeposit)
    {
        if(branchItems < maxItems)
        {
            branchAccount[branchItems] = new Account(customerName);
            branchAccount[branchItems].deposit(initDeposit);
            ++branchItems;
        }
        else
            System.out.println("Sorry " + name + " cannot accept any more accounts!");
    }

    /**
     * Return the Account by position in the collection.  Assumes at least one.
     * @param index assumed 0 .. (getNumberOfAccounts() - 1)
     */
    public Account getAccount(int index)
    {
        return branchAccount[index];
    }

    public String getName()
    {
        return name;
    }

    public int getNumberOfAccounts()
    {
        return branchItems;
    }

    /**
     * Find the Account with Maximum Balance.  Assumes at least one.
     * @return that account.
     */
    public Account searchMaximum()
    {
        Account maxAcc = branchAccount[0];

        for (int i = 1 ; i < branchItems; ++i)
        {
            if (branchAccount[i].getBalance() > maxAcc.getBalance())
            {
                maxAcc = branchAccount[i];
            }
        }
        return maxAcc;
    }


    /**
     * Find the Account with Minimum Balance.  Assumes at least one.
     * @return that account.
     */
    public Account searchMinimum()
    {
        Account minAcc = branchAccount[0];

        for (int i = 1 ; i < branchItems; ++i)
        {
            if (branchAccount[i].getBalance() < minAcc.getBalance())
            {
                minAcc = branchAccount[i];
            }
        }
        return minAcc;
    }
}
```
