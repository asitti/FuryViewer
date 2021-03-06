package com.furyviewer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.furyviewer.domain.Artist;
import com.furyviewer.domain.HatredArtist;

import com.furyviewer.repository.ArtistRepository;
import com.furyviewer.repository.HatredArtistRepository;
import com.furyviewer.repository.UserRepository;
import com.furyviewer.security.SecurityUtils;
import com.furyviewer.web.rest.errors.BadRequestAlertException;
import com.furyviewer.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing HatredArtist.
 */
@RestController
@RequestMapping("/api")
public class HatredArtistResource {

    private final Logger log = LoggerFactory.getLogger(HatredArtistResource.class);

    private static final String ENTITY_NAME = "hatredArtist";

    private final HatredArtistRepository hatredArtistRepository;

    private final UserRepository userRepository;

    private final ArtistRepository artistRepository;

    public HatredArtistResource(HatredArtistRepository hatredArtistRepository, UserRepository userRepository, ArtistRepository artistRepository) {
        this.hatredArtistRepository = hatredArtistRepository;
        this.userRepository = userRepository;
        this.artistRepository = artistRepository;
    }

    /**
     * POST  /hatred-artists : Create a new hatredArtist.
     *
     * @param hatredArtist the hatredArtist to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hatredArtist, or with status 400 (Bad Request) if the hatredArtist has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hatred-artists")
    @Timed
    public ResponseEntity<HatredArtist> createHatredArtist(@RequestBody HatredArtist hatredArtist) throws URISyntaxException {
        log.debug("REST request to save HatredArtist : {}", hatredArtist);
        if (hatredArtist.getId() != null) {
            throw new BadRequestAlertException("A new hatredArtist cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Optional<HatredArtist> existingHatredArtist = hatredArtistRepository.findByArtistAndUserLogin(hatredArtist.getArtist(), SecurityUtils.getCurrentUserLogin());

        if(existingHatredArtist.isPresent()){
            hatredArtist=existingHatredArtist.get();
            hatredArtist.setHated(!hatredArtist.isHated());
        }

        hatredArtist.setDate(ZonedDateTime.now());
        hatredArtist.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get());

        HatredArtist result = hatredArtistRepository.save(hatredArtist);
        return ResponseEntity.created(new URI("/api/hatred-artists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/hatred-artists/Artist/{id}")
    @Timed
    public ResponseEntity<HatredArtist> createHatredArtist(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to save HatredSeries : {}", id);

        Artist artist = artistRepository.findOne(id);

        HatredArtist ha = new HatredArtist();
        ha.setArtist(artist);
        ha.setHated(true);

        return createHatredArtist(ha);
    }

    /**
     * PUT  /hatred-artists : Updates an existing hatredArtist.
     *
     * @param hatredArtist the hatredArtist to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hatredArtist,
     * or with status 400 (Bad Request) if the hatredArtist is not valid,
     * or with status 500 (Internal Server Error) if the hatredArtist couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hatred-artists")
    @Timed
    public ResponseEntity<HatredArtist> updateHatredArtist(@RequestBody HatredArtist hatredArtist) throws URISyntaxException {
        log.debug("REST request to update HatredArtist : {}", hatredArtist);
        if (hatredArtist.getId() == null) {
            return createHatredArtist(hatredArtist);
        }
        HatredArtist result = hatredArtistRepository.save(hatredArtist);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hatredArtist.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hatred-artists : get all the hatredArtists.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hatredArtists in body
     */
    @GetMapping("/hatred-artists")
    @Timed
    public List<HatredArtist> getAllHatredArtists() {
        log.debug("REST request to get all HatredArtists");
        return hatredArtistRepository.findAll();
        }

    /**
     * GET  /hatred-artists/:id : get the "id" hatredArtist.
     *
     * @param id the id of the hatredArtist to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hatredArtist, or with status 404 (Not Found)
     */
    @GetMapping("/hatred-artists/{id}")
    @Timed
    public ResponseEntity<HatredArtist> getHatredArtist(@PathVariable Long id) {
        log.debug("REST request to get HatredArtist : {}", id);
        HatredArtist hatredArtist = hatredArtistRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hatredArtist));
    }

    @GetMapping("/hatred-artists/user/{id}")
    @Timed
    public ResponseEntity<Map<String,Boolean>> getHatredArtistUser(@PathVariable Long id) {
        log.debug("REST request to get HatredArtist : {}", id);
        Boolean hatredArtist = hatredArtistRepository.getIfHatredUser(id, SecurityUtils.getCurrentUserLogin());
        Map<String, Boolean> likeMap = new HashMap<>();
        likeMap.put("like", hatredArtist);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(likeMap));
    }
    @GetMapping("/num-hatred-artists/{id}")
    @Timed
    public Long getNumHatredArtist(@PathVariable Long id) {
        log.debug("REST request to get HatredArtist : {}", id);
        return hatredArtistRepository.NumHatredArtist(id);
       // return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hatredArtist));
    }

    /**
     * DELETE  /hatred-artists/:id : delete the "id" hatredArtist.
     *
     * @param id the id of the hatredArtist to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hatred-artists/{id}")
    @Timed
    public ResponseEntity<Void> deleteHatredArtist(@PathVariable Long id) {
        log.debug("REST request to delete HatredArtist : {}", id);
        hatredArtistRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/hatred-artist/count-hatred-artist/{id}")
    @Timed
    public ResponseEntity<Integer> hatredArtist(@PathVariable Long id) {
        log.debug("REST request to get number of hates of artist");
        Integer num = Math.toIntExact(hatredArtistRepository.countHatredArtist(id));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(num));
    }



}
