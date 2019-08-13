package locatortutorial.css;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.annotations.Managed;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SerenityRunner.class)
public class Exercises {

    @Managed
    WebDriver driver;

    Solutions.ThePage onThePage;

    @Before
    public void openThePage() {
        onThePage.open();
    }

    //
    // Locating elements by ID
    //

    /**
     * TODO: Locate the firstNameField field by ID
     */
    @Test
    public void locate_firstName_by_id() {
        assertThat(onThePage.firstNameField.getAttribute("placeholder")).isEqualTo("Enter first name");
    }

    /**
     * TODO: Locate the surnameField field by ID
     */
    @Test
    public void locate_surname_by_id() {
        assertThat(onThePage.surnameField.getAttribute("placeholder")).isEqualTo("Enter surnameField");
    }

    //
    // Locating elements by CSS class
    //

    /**
     * Todo: Locate the postage cost value
     */
    @Test
    public void locate_postage_cost() {
        assertThat(onThePage.postage.getText()).isEqualTo("5");
    }

    /**
     * TODO: Locate the sales tax value using a CSS class
     */
    @Test
    public void locate_sales_tax() {
        assertThat(onThePage.salesTax.getText()).isEqualTo("20");
    }

    //
    // Locating elements by attribute
    //

    /**
     * TODO: Locate the emailField field using a CSS attribute
     */
    @Test
    public void locate_email_field_by_name() {
        assertThat(onThePage.emailField.getAttribute("placeholder")).isEqualTo("Enter emailField");
    }

    /**
     * TODO: Locate the passwordField field using a CSS attribute
     */
    @Test
    public void locate_password_field_by_name() {
        assertThat(onThePage.passwordField.getAttribute("placeholder")).isEqualTo("Password");
    }

    //
    // Locating child elements
    //

    /**
     * TODO: Locate the order details title
     */
    @Test
    public void locate_order_details_title() {
        assertThat(onThePage.orderDetailsTitle.getText()).isEqualTo("Order");
    }

    /**
     * TODO: Locate the total price value using a child element contruct
     */
    @Test
    public void locate_total_price() {
        assertThat(onThePage.totalPrice.getText()).isEqualTo("125");
    }

    /**
     * TODO: Locate all the child elements in the country dropdown
     */
    @Test
    public void locate_all_countries() {
        assertThat(onThePage.getCountries()).contains("England", "France", "Ireland", "Italy", "Scotland", "Wales");
    }

    //
    // Locating indirect children
    //

    /**
     * TODO: Locate all the available colors
     */
    @Test
    public void locate_available_colors() {
        assertThat(onThePage.getAvailableColors()).contains("Blue", "Red", "Yellow", "Green");
    }

    /**
     * TODO: Locate all the unavailable colors
     */
    @Test
    public void locate_unavailable_colors() {
        assertThat(onThePage.getUnavailableColors()).contains("Cyan", "Grey");
    }


    //
    // Locating specific elements
    //
    @Test
    public void locate_2nd_available_color() {
        assertThat(onThePage.nthAvailableColor(2)).isEqualTo("Red");
    }

    /**
     * TODO: Find the 2nd unavailable color
     */
    @Test
    public void locate_2nd_unavailablecolor() {
        assertThat(onThePage.nthUnavailableColor(2)).isEqualTo("Grey");
    }

    /**
     * TODO: Locate the street field using a partial CSS attribute
     */
    @Test
    public void locate_field_by_partial_attribute_value() {
        assertThat(onThePage.street.getAttribute("placeholder")).isEqualTo("Enter Street");
    }

    /**
     * TODO: Locate the city field using a partial CSS attribute
     */
    @Test
    public void locate_password_field_by_partial_attribute_value() {
        assertThat(onThePage.city.getAttribute("placeholder")).isEqualTo("Enter City");
    }

    @DefaultUrl("classpath:site/index.html")
    public static class ThePage extends PageObject {

        @FindBy(css = "#firstNameField")
        public WebElementFacade firstName;

        @FindBy(css = "#surnameField")
        public WebElementFacade surname;

        @FindBy(css = "input[name='emailField']")
        public WebElementFacade email;

        @FindBy(css = "input[name='passwordField']")
        public WebElementFacade password;

        @FindBy(css = "input[id$='city']")
        public WebElementFacade city;

        @FindBy(css = "input[id$='street']")
        public WebElementFacade street;

        @FindBy(css = ".postage-cost")
        public WebElementFacade postage;

        @FindBy(css = ".sales-tax")
        public WebElementFacade salesTax;

        @FindBy(css = "#order-details > .card-title")
        public WebElementFacade orderDetailsTitle;

        @FindBy(css = "#total-price > .amount")
        public WebElementFacade totalPrice;

        @FindBy(css = "select[name='country'] > option")
        public List<WebElementFacade> countries;

        public List<String> getCountries() {
            return countries.stream().map(WebElementFacade::getText).collect(Collectors.toList());
        }

        public List<String> getAvailableColors() {
            return findAll("#available-colors .color")
                    .stream().map(WebElementFacade::getText).collect(Collectors.toList());
        }

        public List<String> getUnavailableColors() {
            return findAll("#unavailable-colors .color")
                    .stream()
                    .map(WebElementFacade::getText)
                    .collect(Collectors.toList());
        }

        public String nthAvailableColor(int index) {
            return find("#available-colors span:nth-child(" + index + ")").getText();
        }

        public String nthUnavailableColor(int index) {
            return find("#unavailable-colors span:nth-child(" + index + ")").getText();
        }
    }
}
