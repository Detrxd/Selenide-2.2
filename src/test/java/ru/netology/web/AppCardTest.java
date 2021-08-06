package ru.netology.web;


//import org.checkerframework.checker.units.qual.A;

import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class AppCardTest {


    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
    }

    @AfterEach
    void tearDown() {
        close();
    }

    private String formatDeliveryDate(int plusDays) {
        LocalDate localDate = LocalDate.now();
        LocalDate deliveryDate = localDate.plusDays(plusDays);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dateText = deliveryDate.format(formatter);
        return dateText;
    }

    @Test
    void ShouldFillInTheGaspAllChecksPassed() {
        $("[data-test-id='city']").$("[placeholder='Город']").setValue("Астрахань");
        $("[data-test-id='date'").$("[placeholder='Дата встречи']").setValue(formatDeliveryDate(3));
        $("[data-test-id='name']").$("[name='name']").setValue("Филимонов Илья");
        $("[data-test-id='phone']").$("[name='phone']").setValue("+79163273696");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='notification']").shouldBe(visible, Duration.ofSeconds(15)).
                shouldHave(exactText("Успешно!\n" + "Встреча успешно забронирована на " + formatDeliveryDate(3)));
    }

    @Test
    void emptyValueFieldCity() {
        $("[data-test-id='city']").$("[placeholder='Город']").setValue("");
        $("[data-test-id='date']").$("[placeholder='Дата встречи']").setValue(formatDeliveryDate(3));
        $("[data-test-id='name']").$("[name='name']").setValue("Филимонов Илья");
        $("[data-test-id='phone']").$("[name='phone']").setValue("+79163273696");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldBe(visible)
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void emptyValueFieldName() {
        $("[data-test-id='city']").$("[placeholder='Город']").setValue("Астрахань");
        $("[data-test-id='date']").$("[placeholder='Дата встречи']").setValue(formatDeliveryDate(3));
        $("[data-test-id='name']").$("[name='name']").setValue("");
        $("[data-test-id='phone']").$("[name='phone']").setValue("+79163273696");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldBe(visible)
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void emptyValueFieldPhone() {
        $("[data-test-id='city']").$("[placeholder='Город']").setValue("Астрахань");
        $("[data-test-id='date']").$("[placeholder='Дата встречи']").setValue(formatDeliveryDate(3));
        $("[data-test-id='name']").$("[name='name']").setValue("Филимонов Илья");
        $("[data-test-id='phone']").$("[name='phone']").setValue("");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldBe(visible)
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void notCheckVerifyBox() {
        $("[data-test-id='city']").$("[placeholder='Город']").setValue("Астрахань");
        $("[data-test-id='date']").$("[placeholder='Дата встречи']").setValue(formatDeliveryDate(3));
        $("[data-test-id='name']").$("[name='name']").setValue("Филимонов Илья");
        $("[data-test-id='phone']").$("[name='phone']").setValue("+79163273696");
//        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='agreement'].input_invalid .checkbox__text").shouldBe(visible)
                .shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    void incorrectCityName() {
        $("[data-test-id='city']").$("[placeholder='Город']").setValue("Пекин");
        $("[data-test-id='date']").$("[placeholder='Дата встречи']").setValue(formatDeliveryDate(3));
        $("[data-test-id='name']").$("[name='name']").setValue("Филимонов Илья");
        $("[data-test-id='phone']").$("[name='phone']").setValue("+79163273696");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldBe(visible)
                .shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void symbolInNameField() {
        $("[data-test-id='city']").$("[placeholder='Город']").setValue("Астрахань");
        $("[data-test-id='date']").$("[placeholder='Дата встречи']").setValue(formatDeliveryDate(3));
        $("[data-test-id='name']").$("[name='name']").setValue("Фил%имон#в Илья");
        $("[data-test-id='phone']").$("[name='phone']").setValue("+79163273696");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldBe(visible)
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void passDoubleSecondName() {
        $("[data-test-id='city']").$("[placeholder='Город']").setValue("Астрахань");
        $("[data-test-id='date']").$("[placeholder='Дата встречи']").setValue(formatDeliveryDate(3));
        $("[data-test-id='name']").$("[name='name']").setValue("Филимонов-Щедрин Илья");
        $("[data-test-id='phone']").$("[name='phone']").setValue("+79163273696");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='notification']").shouldBe(visible, Duration.ofSeconds(15)).
                shouldHave(exactText("Успешно!\n" + "Встреча успешно забронирована на " + formatDeliveryDate(3)));
    }

    @Test
    void numberName() {
        $("[data-test-id='city']").$("[placeholder='Город']").setValue("Астрахань");
        $("[data-test-id='date']").$("[placeholder='Дата встречи']").setValue(formatDeliveryDate(3));
        $("[data-test-id='name']").$("[name='name']").setValue("1234 Илья");
        $("[data-test-id='phone']").$("[name='phone']").setValue("+79163273696");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldBe(visible).
                shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void phoneNumberOverLimit() {
        $("[data-test-id='city']").$("[placeholder='Город']").setValue("Астрахань");
        $("[data-test-id='date']").$("[placeholder='Дата встречи']").setValue(formatDeliveryDate(3));
        $("[data-test-id='name']").$("[name='name']").setValue("Филимонов Илья");
        $("[data-test-id='phone']").$("[name='phone']").setValue("+79163273696666");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldBe(visible).
                shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void phoneNumberUnderLimit() {
        $("[data-test-id='city']").$("[placeholder='Город']").setValue("Астрахань");
        $("[data-test-id='date']").$("[placeholder='Дата встречи']").setValue(formatDeliveryDate(3));
        $("[data-test-id='name']").$("[name='name']").setValue("Филимонов Илья");
        $("[data-test-id='phone']").$("[name='phone']").setValue("+791636");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldBe(visible).
                shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void badValueInFieldCity() {
        $("[data-test-id='city']").$("[placeholder='Город']").setValue("Астра");
        $("[data-test-id='date']").$("[placeholder='Дата встречи']").setValue(formatDeliveryDate(3));
        $("[data-test-id='name']").$("[name='name']").setValue("Филимонов Илья");
        $("[data-test-id='phone']").$("[name='phone']").setValue("+79163273696");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldBe(visible).
                shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

}


