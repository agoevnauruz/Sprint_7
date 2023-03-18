package ru.yandex.praktikum.courierTest;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.courier.Courier;
import ru.yandex.praktikum.courier.CourierClient;
import ru.yandex.praktikum.courier.CourierGenerator;
import ru.yandex.praktikum.courier.Credentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.junit.Assert.assertEquals;

public class CourierNegativeTest {
    private  CourierClient courierClient;
    private  Courier courier;
    private int id;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.defaultCourier;
        ValidatableResponse responseCreate = courierClient.create(courier);
    }

    @After
    public void cleanUp(){

        courierClient.delete(id);
    }

    @Test
    @DisplayName("Проверка ответа когда курьер пытается зарегистрироваться дважды" )
    public void createTheSameCourierAndCheckStatusCode(){
        ValidatableResponse responseCreate = courierClient.create(courier);
        ValidatableResponse responseLogin = courierClient.login(Credentials.from(courier));
        id = responseLogin.extract().path("id");
        int actualCode = responseCreate.extract().path("code" );
        String actualMessage = responseCreate.extract().path("message" );
        assertEquals("Ошибка 409",SC_CONFLICT, actualCode);
        assertEquals("Этот логин уже используется. Попробуйте другой.",actualMessage);
    }
}

