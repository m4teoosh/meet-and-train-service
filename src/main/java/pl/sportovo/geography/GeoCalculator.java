package pl.sportovo.geography;

public class GeoCalculator {
    public static double covertKilometersToLatitudeDegrees(int kilometers) {
        return kilometers / 110.574;

    }
    public static double covertKilometersToLongitudeDegrees(double latitude, int kilometers) {
        return kilometers / (111.320 * Math.cos(Math.toRadians(latitude)));
    }

    public static double calculateDistanceInKilometers(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
