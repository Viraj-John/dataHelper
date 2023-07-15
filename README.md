**Data Helper V.1.0.0**

**Usage **

Identify Test Data from API Calls and Filtering them using string parameters checking contains condition

**Application Specifications ** 

UI on Java Swing using Miglayout for Responsive UI
Concurrent calls to API hence real time update of execution status and response writing 
Soap and REST Apis are handled

**Requirement :  **

Java 13 or higher

**Fields and Usage **

<img width="862" alt="Screenshot 2023-07-15 at 9 55 19 AM" src="https://github.com/Viraj-John/dataHelper/assets/138886172/31aa9682-298b-4ced-b37a-199ac8e9e9ec">

URL [Text Field]
The Resource URL

Input [Text Field]
Text file which containing the  Data used for iteration

Output Folder [Text Field]
Folder where the api response will be written 

Filter [Text Field]
{string} : Checks the filter value is available in Response it can be a tag or value or both

Skip [Text Field]
{Integer} : used to skip data  from iteration 
Example : If 1000 records are there and the skip is 100 then iteration/execution  start from 101 to 1000

REST [Checkbox]
If Checked it is REST Api call 
{
If Request is Empty it will trigger a GET Request 
Else a Post Request
}
Else  it is SOAP Protocol call 

Client ID [Text Field]
Optional can pass it if required in Headers

Request [Text Area] - Scrollable 
{data} : add this string where ever the iteration string needed to be replaced
Request Body can be JSON format or XML format

Log [Text Area] - Scrollable 
Log will be updated on runtime according to the filter provided





