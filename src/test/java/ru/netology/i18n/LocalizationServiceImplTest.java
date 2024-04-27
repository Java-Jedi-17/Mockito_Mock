package ru.netology.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;

class LocalizationServiceImplTest {

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
}