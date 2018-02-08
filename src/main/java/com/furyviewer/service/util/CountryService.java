package com.furyviewer.service.util;

import com.furyviewer.domain.Country;
import com.furyviewer.repository.CountryRepository;
import com.furyviewer.service.GoogleMaps.Service.GoogleMapsDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CountryService {
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private GoogleMapsDTOService googleMapsDTOService;

    @Autowired
    private NAEraserService naEraserService;

    /**
     * Método que se encarga de buscar en la base de datos una Country y en caso de no existir la crea.
     * @param countryName String | Nombre de la country devuelto por la api.
     * @return Country | Country creado o encontrado en la base de datos.
     */
    public Country importCountry(String countryName) {
        Country country = null;

        if (naEraserService.eraserNA(countryName) != null) {
            //Buscamos countryName
            Optional<Country> c = countryRepository.findByName(googleMapsDTOService.getName(countryName));

            if (c.isPresent()) {
                country = c.get();
            } else {
                country = googleMapsDTOService.importCountry(countryName);
            }
        }
        return country;
    }
}
