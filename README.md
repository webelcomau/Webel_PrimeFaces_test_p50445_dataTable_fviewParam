# Webel_PrimeFaces_test_p50445_dataTable_fviewParam

A mini [NetBeans IDE](https://netbeans.org/) Ant-based web app prepared by Darren Kelly (a.k.a. "Dr Darren") of [Webel IT Australia](https://www.webel.com.au)  for testing and demonstrating a suspected issue with the `p:dataTable` of the [PrimeFaces](https://www.primefaces.org/) UI framework/toolkit. Please use NetBeans IDE 8.2 and the bundled Glassfish 4.1.1.

For a description of the suspected issue/concern please visit:

- Primefaces community forum: [p:dataTable how prevent f:viewParam being invoked on 1st rowEdit then tick save using JSF at XHTML level (not in bean)](https://forum.primefaces.org/viewtopic.php?f=3&t=50445&p=154868#p154868)
- Stackoverflow: [p:dataTable how prevent f:viewParam being invoked on 1st rowEdit then “tick save” using JSF at XHTML level (not in Java in backing bean)](http://stackoverflow.com/questions/43408949/pdatatable-how-prevent-fviewparam-being-invoked-on-1st-rowedit-then-tick-save?noredirect=1#comment73878640_43408949).

*There are also detailed instructions when you run the web app, and in this README below.*

------

Please provide feedback via the forum links above. I have invested some effort in analysing this because I use `p:dataTable` "embedded" in edit forms together with `f:viewParam` a lot, and I want to understand this matter better, even if it is not an actual problem with `p:dataTable`.

------

### Versions

Out-of-the-box the Mojarra JSF 2.2.12 as bundled with Glassfish4.1.1 for NetBeans IDE 8.2 is used.

Out-of-the-box PrimeFaces 6.0 community edition is used (and more recent versions like PrimeFaces 6.1RC2 seem to behave the same way). 

See below for instructions on how to easily change the Mojarra or PrimeFaces version.

***When you run the web app a header on each JSF page dynamically interrogates the Mojarra, PrimeFaces and Java versions. :smiley:***

------

## HOWTO run it and reproduce the issue/concern

Please just [download the master ZIP-archive](https://github.com/webelcomau/Webel_PrimeFaces_test_p50445_dataTable_fviewParam/archive/master.zip), unzip it, and open it in NetBeans IDE 8.2. **You don't need to Git-clone the project.** Make sure you have Glassfish-4.1.1 set as the server for the project.

When you run the web app an index (home) page will load. At the bottom of the index page there is a single link to a test page, which contains an edit form for a "fake" Element entity, where an Element can have one more more Link items. There is an "embedded" `p:dataTable` for "interim editing" of the Links, with URL validation. (For this example a JSF validator is not used, validation is done in a rowEdit listener, but the result is the same.)

The test web app uses a `FakeEntity`/`FakeQuery` system (no real database), and pre-loads a couple of Element entities. You will be testing the Element with id=0, which initially has a couple of valid Link items. Just click on the test link from the home page to load it. It will load: 

`/test/test_dataTable_p154868.xhtml?id=0`

1. On initial @ViewScoped test page load, a `f:viewParam` for `setId(Long)` is invoked, which loads from a "fake" database a fake entity Element (which carries Links) for editing (assigned to `#{viewBean.selected}`). Any changes made to Links using the *Links editor* table at the bottom are not to be actually saved to database until the entire edit form is submitted using a `p:commandButton`. But interim changes may be seen in the updated read-only **Links view** fieldset above it.
2. Perform a row edit on the 1st row of the embedded Links editor table. Change the Link URL and edit an invalid URL such as "invalid1". The rowEdit listener is `{viewBean.onLinkRowEdit(RowEditEvent event)`, which performs a validation check on the submitted URL, but on the first "tick save" **it does not detect an invalid URL, and the server log echo shows that the URL it has received has not changed.** This can also been seen by an INFO level p:messages receipt.
3. *The reason:* On the initial "tick save" the `f:viewParam` for `setId(Long)` is invoked **again** (**BEFORE** the rowEdit listener) and reloads from database the Element that carries the Links, over-writing the interim changes made to the edited Link. This can be seen by an echo in the server log. By the time the Link's URL reaches the row edit listener it has been reverted to the original value (but the updated read-only *Links View* above the Links editor table shows the "apparently edited" value).
4. Then, perform another row edit **on the same 1st row** of the embedded Links editor table, this time enter "invalid2" as a URL.
5. On performing "tick save" again the `f:viewParam` for `setId(Long)` **is NOT invoked**, so the Element (and its Links) are not reloaded. Thus the changes made to the edited Link are this time received correctly by the row edit listener, and the URL validation check fails correctly (see the logged error message and the p:messages ERROR receipt).

------

### How to configure alternative Mojarra (JSF implementation) and PrimeFaces JAR libraries for comparative tests

Often to investigate an issue one needs to experiment with different library versions and compare test runs.  This is quite easily done with this mini NetBeans web app. By default this web app will use the Mojarra bundled with your web app server (Glassfish as bundled with NetBeans, or Payara if you installed it extra), and a recent PrimeFaces community edittion version is included as a JAR under the `./lib` folder.

**To run against a specific Mojarra (or other JSF implementation) version:**

- There are some Mojarra versions already under the `./lib` folder for you to choose from, or, if you need to test against another version:

  - Download a Mojarra implementation JAR [from the Glassfish Maven repository](https://maven.java.net/content/repositories/releases/org/glassfish/javax.faces/) (you don't need Maven) and place the clearly named and versioned `javax.faces-[version].jar` in the `./lib` folder of your project. DO NOT over-write your server's version :cop:, as it may impact on other JSF web applications, and it's a bit tedious if you want to experiment quickly.

- Assign one from the NetBeans IDE *Projects* window from the **Libraries** node for your project by right clicking and using just **Add JAR** (not as a full NetBeans Library) and **use the relative path** option.

- Then uncomment BOTH LINES indicated in the `/web/WEB-INF/glassfish-web.xml` to `useBundledJsf`:

- ```xml
  <class-loader delegate="false" />
  <property name="useBundledJsf" value="true" />
  ```

- **Clean and Build** then **Run**; the `common.xhtml` header will show you whether it caught the new version.

**To run a specific Primefaces version:**

- You can directly download [Community Edition JARs for PrimeFaces](https://www.primefaces.org/downloads/)  from the main PrimeFaces site (you may have to scroll down a bit) or latest ELITE and PRO versions if you are a subscribed customer.
- Place a clearly named and versioned `primefaces-[version].jar` in the `./lib` folder of your project.
- In the *Projects* window under the **Libraries** node for your project you might have to remove any existing Primefaces jar library assignment. (It won't delete the actual JAR from the `./lib`, it just un-assigns it.)
- Assign the new one from the *Projects* window from the **Libraries** node for your project by right-clicking and using just **Add JAR** (not as a full NetBeans Library) and **use the relative path** option.
- **Clean and Build** then **Run**; the `common.xhtml` header will show you whether it caught the new version.

------

#### How this mini test web app was prepared (so easily)

This mini test web app was spawned from the new [NetBeans Ant-based PrimeFaces test template](https://github.com/webelcomau/Webel_PrimeFaces_test_template_NetBeans_Ant/tree/master/web) project from Webel IT Australia. Please see the README there for details about the FakeQuery/FakeEntity system used behind the scenes to get test apps like this up and running quickly. 

------

### 