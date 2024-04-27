package ru.netology.sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationServiceImpl;
import java.util.HashMap;

public class MessageSenderImplTests {

    @Test
    void sends_text() {
        var geoService = Mockito.spy(GeoServiceImpl.class);
        var localizationService = Mockito.spy(LocalizationServiceImpl.class);
        var messageSender = new MessageSenderImpl(geoService, localizationService);

        HashMap mapRu = new HashMap();
        mapRu.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.0.0.0");
        HashMap mapENG = new HashMap();
        mapENG.put(MessageSenderImpl.IP_ADDRESS_HEADER, "96.0.0.0");

        String expectedENG = "Welcome";
        String expectedRu = "Добро пожаловать";
        String preferencesENG = messageSender.send(mapENG);
        String preferencesRu = messageSender.send(mapRu);

        Assertions.assertEquals(expectedRu, preferencesRu);
        Assertions.assertEquals(expectedENG, preferencesENG);
    }

    @Test
    void checking_the_returned_text() {
        String expected = "Добро пожаловать";
        String preferences = new LocalizationServiceImpl().locale(Country.RUSSIA);
        Assertions.assertEquals(expected, preferences);
    }

    @Test()
    void testExpectedException() {
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            var exception = new GeoServiceImpl();
            exception.byCoordinates(1, 1);
        });
        Assertions.assertEquals("Not implemented", thrown.getMessage());
    }
}
