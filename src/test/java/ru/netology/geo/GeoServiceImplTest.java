package ru.netology.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

class GeoServiceImplTest {

    @Test
    void tests_to_verify_location_by_ip() {
        String ip = "96.0.0.0";
        var expected = new Location("New York", Country.USA, " 10th Avenue", 32);
        var geoService = new GeoServiceImpl();
        Location preferences = geoService.byIp(ip);
        Assertions.assertEquals(expected.getCountry(), preferences.getCountry());
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