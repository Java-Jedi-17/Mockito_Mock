package ru.netology.sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationServiceImpl;
import java.util.HashMap;

public class MessageSenderImplTests {

    @Test
    void russian() {
        String ip = "172.0.0.0";
        HashMap mapRus = new HashMap();
        mapRus.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);

        var geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(ip))
                .thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));

        var localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn("Добро пожаловать");

        var messageSender = new MessageSenderImpl(geoService, localizationService);

        String preferences = messageSender.send(mapRus);
        String expected = "Добро пожаловать";

        Assertions.assertEquals(expected, preferences);
    }

    @Test
    void english() {
        String ip = "96.0.0.0";
        HashMap mapENG = new HashMap();
        mapENG.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);

        var geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(ip))
                .thenReturn(new Location("New York", Country.USA, null, 0));

        var localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale(Country.USA))
                .thenReturn("Welcome");

        var messageSender = new MessageSenderImpl(geoService, localizationService);

        String preferences = messageSender.send(mapENG);
        String expected = "Welcome";

        Assertions.assertEquals(expected, preferences);

        Mockito.verify(localizationService, Mockito.times(2))
                .locale(Mockito.<Country>any());

        Mockito.verify(geoService, Mockito.times(1))
                .byIp(Mockito.<String>any());
    }

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
    void tests_to_verify_location_by_ip() {
        String ip = "96.0.0.0";
        var expected = new Location("New York", Country.USA, " 10th Avenue", 32);
        var geoService = new GeoServiceImpl();
        Location preferences = geoService.byIp(ip);
        Assertions.assertEquals(expected.getCountry(), preferences.getCountry());
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
