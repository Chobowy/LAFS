package zut.lafs.storage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import zut.lafs.model.Claim;
import zut.lafs.model.Item;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {
    private final ObjectMapper mapper = new ObjectMapper();
    private final File itemsFile = new File("data/items.json");
    private final File claimsFile = new File("data/claims.json");

    public FileStorage() {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        itemsFile.getParentFile().mkdirs();
    }

    public List<Item> loadItems() {
        try {
            if (!itemsFile.exists()) return new ArrayList<>();
            return mapper.readValue(itemsFile, new TypeReference<List<Item>>() {});
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read items file", e);
        }
    }

    public void saveItems(List<Item> items) {
        try {
            mapper.writerFor(new TypeReference<List<Item>>() {}).writeValue(itemsFile, items);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not save items file", e);
        }
    }

    public List<Claim> loadClaims() {
        try {
            if (!claimsFile.exists()) return new ArrayList<>();
            return mapper.readValue(claimsFile, new TypeReference<List<Claim>>() {});
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read claims file", e);
        }
    }

    public void saveClaims(List<Claim> claims) {
        try {
            mapper.writeValue(claimsFile, claims);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not save claims file", e);
        }
    }
}