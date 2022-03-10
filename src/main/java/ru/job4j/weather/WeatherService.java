package ru.job4j.weather;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    private final Map<Integer, Weather> weathers = new ConcurrentHashMap<>();

    public WeatherService() {
        weathers.put(1, new Weather(1, "Msc", 20));
        weathers.put(2, new Weather(2, "SPb", 15));
        weathers.put(3, new Weather(3, "Bryansk", 17));
        weathers.put(4, new Weather(4, "Smolensk", 15));
        weathers.put(5, new Weather(5, "Kiev", 21));
        weathers.put(6, new Weather(6, "Minsk", 30));
    }

    public Mono<Weather> findById(Integer id) {
        return Mono.justOrEmpty(weathers.get(id));
    }

    public Flux<Weather> all() {
        return Flux.fromIterable(weathers.values());
    }

    public Mono<Weather> getCityMaxTemp() {
        int rsl = weathers.values().stream()
                .map(Weather::getTemperature)
                .max(Comparator.naturalOrder()).get();
        Weather weather = weathers.values().stream()
                .filter(weather1 -> weather1.getTemperature() == rsl)
                .findFirst().get();
        return Mono.just(weathers.get(weather.getId()));
    }

    public Flux<Weather> getCityGreatThen(Integer temperature) {
        return Flux.fromIterable(
                weathers.values().stream()
                .filter(weather -> weather.getTemperature() > temperature)
                .collect(Collectors.toList())
        );
    }

}
