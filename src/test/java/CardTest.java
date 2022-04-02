import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.Keys.BACK_SPACE;

public class CardTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }
    @Test
    void shouldRegisterCard(){
        Configuration.holdBrowserOpen=true;
        open("http://localhost:9999");
        RegistrationInfo registration= DataGenerator.Registration.generateInfo("ru");
        String planeData1 = DataGenerator.generateDate(4);
        String planeData2 = DataGenerator.generateDate(7);

        $("[data-test-id='city'] input").setValue(registration.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planeData1);
        $("[data-test-id='name'] input").setValue(registration.getName());
        $("[data-test-id='phone'] input").setValue(registration.getPhone());
        $("[data-test-id='agreement'] .checkbox__text").click();
        $$(".button__text").find(Condition.text("Запланировать")).click();
        $(".notification__content").shouldBe(Condition.visible).shouldHave(exactText("Встреча успешно запланирована на " + planeData1), Duration.ofSeconds(15));
        $(".notification__content").click();

        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planeData2);
        $$(".button__text").find(exactText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content").shouldBe(Condition.visible).shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $$(".button__text").find(exactText("Перепланировать")).click();
        $(".notification__content").shouldBe(Condition.visible).shouldHave(exactText("Встреча успешно запланирована на " + planeData2), Duration.ofSeconds(15));

    }
}
