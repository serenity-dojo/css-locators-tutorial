# CSS Locators for Web Testing - a tutorial

XPath is widely used in web testing.
However, experience shows us that XPath selectors are in many (if not most) most cases the worst option when it comes to readability and maintainability.
In this tutorial, you will learn how to use CSS to write more readable and more robust WebDriver locators.

Why should we (mostly) prefer CSS over XPath when choosing a locator strategy?
The most important reason is maintainability.
CSS locators are more concise and easier to read (once you are familiar with the CSS language).
It is generally easier to make robust, flexible selectors using CSS (the equivalent selectors in XPath will be longer and harder to read).
In addition, CSS is natively supported by all browsers, which can lead to performance improvements in some situations.

## Locating elements in Selenium

Before we look at CSS in detail, here is a brief recap on the various ways we can locate elements on a page in our WebDriver tests.

### FindBy annotations

One common approach is to use the `@FindBy` annotations inside a page object. If you are using XPath locators, you might see code like the following:

```java
    @FindBy(xpath = "//input[@id='firstName']")
    private WebElement firstNameField;

    public void enterFirstName(String firstName) {
        firstNameField.sendKeys(firstName);
    }
```

### Lean Page Objects and Action Classes

Many more experienced teams find that Page Objects often become large and unwieldy, and prefer to apply a stronger separation of concerns between locating the elements on a page, and interacting with them. This approach is known as Lean Page Objects. In Lean Page Objects, or when using the Screenplay pattern, we locate elements using a `By` class or even a simple String:

```java
    public static final By FIRST_NAME = By.xpath("//input[@id='firstName']");
```

Or

```java
    public static final String SURNAME = "//input[@id='surname']";
```

These locators are used in methods that are defined in Action Classes or Screenplay tasks, e.g.

```java
    public void enterUserName(UserDetails newUser) {
      $(FIRST_NAME).sendKeys(newUser.getFirstName());
      $(SURNAME).sendKeys(newUser.getLastName());
    }
```

### Finding collections

We often need to retrieve collections of related objects, rather than a specific value.
For example, suppose we needed to read the list of available sizes for an item displayed on the screen.

```java
    @FindBy(xpath = "//div[@class='product-details']//select[@name='size']/option")
    private List<WebElement> sizeOptions;
    ...
    public List<String> getAvailableSizes() {
      return sizeOptions.stream()
      .map(WebElementFacade::getText)
      .collect(Collectors.toList());      
    }

```

Alternatively, using Action Classes in Serenity, it is common to locate the collection of elements and convert them into a more usable format on-the-fly:

```java
public static final String PRODUCT_SIZES = "//div[@class='product-details']//select[@name='size']/option";
...
public List<String> getAvailableSizes() {
    return findAll(CART_ITEMS).stream()
            .map(WebElementFacade::getText)
            .collect(Collectors.toList());
}
```

All of these approaches can benefit from using CSS locators rather than XPath. In the next section, we will learn how to locate elements on a page using CSS.

## CSS Locators

### Locating elements by ID

Identifiers are generally the most reliable way to locate an element on a page.
In Selenium, we can use the `By.id()` locator to locate an element using the identifier.
However there are times when using a CSS selector is also convenient.

In XPath, we can locate an element with a given ID using the `@id` attribute, like this:

```
//input[@id='firstName']
```

In CSS, we can use the _'#'_ symbol to locate an `input` element with a given ID:
```
input#firstName
```

However since the ID uniquely identifies an element, we can drop the `input` tag name, so our CSS expression becomes the following:
```
#firstName
```

### Locating elements by CSS Class

In well designed web applications, CSS classes are not only used to define the graphical styling, but also to describe the elements on the page. For example, in the following code, CSS classes on the various elements help identify the meaning of each piece of information.

```html
<div id="item-prices">
    <ul>
        <li>Blue shirt: $<span class="price right-align item-cost">60</span></li>
        <li>Gray pants: $<span class="price right-align item-cost">40</span></li>
        <li>Postage: $<span class="price right-align postage-cost">5</span></li>
    </ul>
</div>
```

In XPath, we can check the `@class` attribute, but for XPath, the _class_ attribute is an attribute like any other.
An XPath expression to find the postage cost would look like this:
```
//span[contains(@class,'postage-cost')]
```

To locate an element with a given class, we use the _"."_ symbol. For example, we could locate the cost of postage with the following CSS:
```
.postage-cost
```

### Locating elements by attribute

It is also possible to locate elements using values of their attributes.
For example, consider the following HTML code:

```html
<input type="email" class="form-control"
       id="field122" name="email"
       aria-describedby="emailHelp"
       placeholder="Enter email">
```
This `input` field contains an id value, but it does not seem very meaningful.
In this case, the `name` attribute would be a more reliable way to locate the field.
Using XPath, we could use an expression like the following:

```
//input[@name='email']
```

In CSS, we can do something similar:
```
input[name='email']
```

We can also shorten this to match any element with a _name_ attribute equal to "email":
```
[name='email']
```

And if we need to locate an element with several specific attribute values, we can just combine selectors.
Suppose we had the following HTML code:

```html
<input class="btn btn-primary" type="submit" name="user-details">
```

If the _name_ attribute was not unique, we could locate this element by combining the name and the type, like this:

```
[name='email'][type='submit']
```

### Direct Children

In XPath, you use the single-slash "/" to identify the direct child of an element. For example, in the following code, the `h5` element is a direct child of the `div` element.

```html
<div id="order-details" class="card-body">
  <h5 class="card-title">Order</h5>
```

In XPath, you could locate the `h5` element using a direct child operator:
```
//div[@id='order-details']/h5
```

In CSS, we use _">"_ to indicate a direct child. So the equivalent of the previous XPath expression would be the following:
```
#order-details > h5
```

We can also locate elements using other CSS operators, as shown here:
```
#order-details > .card-title
```
This way, if the tag used for the title changes, the locator will not need to be changed.

### Indirect Children

You often need identify an element that is a direct or indirect descendant of another known element. For example, the `h5` element below is an indirect descendant of the top-level `div`:

```html
<div class="card" id="perso" >
    <div class="card-body">
        <h5 class="card-title">Personal Details</h5>
```

In XPath, we use the double-slash operator (_"//"_) to locate direct or indirect descendants:
```
//div[@id='perso']//h5[@class='card-title']
```

In CSS, we just use whitespace:
```
#perso .card-title
```

Both direct and indirect children can be very useful when finding collections of elements. For example, the following HTML describes a collection of badges:

```html
<div id="available-colors">
    <span>Available:</span>
    <div class="color-list">
        <span class="color badge badge-primary">Blue</span>
        <span class="color badge badge-danger">Red</span>
        <span class="color badge badge-warning">Yellow</span>
        <span class="color badge badge-success">Green</span>
    </div>
</div>
```

The following CSS selector will identify the set of four colours shown here:
```
//div[@id='available-colors']//span[contains(@class,'badge')]
```

The equivalent in CSS would be the following:
```
#available-colors .color
```

### Working with collections

We can also use CSS to locate specific element in a collection.
For example, suppose we wanted to find the second color badge in the previous HTML extract.
In XPath, we could do something like this:
```
//div[@id='available-colors']/div/span[2]
```

In CSS, we would use the `nth-child` or `nth-of-type` selector:
```
#available-colors span:nth-child(2)
```

The `nth-child` selector matches element that is the _nth_ element in a collection. So this expression returns the 2nd element in the collection that match the `#available-colors span` CSS selector: 'Red'.

### Working with partial attribute names

Sometimes it is useful to find elements using partial matches on attribute values. For example, suppose we have the following HTML code:

```html
<input class="form-control"
       id="persondetailsform_field127_street"
       placeholder="Enter Street">
```

In XPath, we could locate this element using an expression like the following:
```
//input[contains(@id,'street')]
```

The equivalent in CSS would use the _$=_ operator, which matches an attribute value with a given suffix:
```
input[id$='street']
```

In CSS, we can use the following operators to perform partial matches:

* _^=_: Match a prefix (`input[id^='persondetailsform']`)

* _$=_: Match a suffix (`input[id$='street']`)

* _*=_: Match a substring (`input[id*='details']`)

## When to use XPath

XPath is required if you need to locate an element based on it's text content.
For example, suppose we have the following HTML code:

```html
<button type="button" class="btn btn-primary">Modify order</button>
```

The best way to locate this button would be to use the following XPath:
```
//button[. = 'Modify order']
```
