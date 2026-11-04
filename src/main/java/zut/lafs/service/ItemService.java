package zut.lafs.service;

import zut.lafs.exception.*;
import zut.lafs.model.*;
import zut.lafs.storage.FileStorage;

import java.time.LocalDate;
import java.util.*;

    public class ItemService {
        private final FileStorage storage;
        private final List<Item> items;
        private final List<Claim> claims;
        private final Map<String, User> users = new HashMap<>();

        public ItemService(FileStorage storage) {
            this.storage = storage;
            this.items = storage.loadItems();
            this.claims = storage.loadClaims();
            users.put("ADMIN01", new User("ADMIN01", "Dean of Students Office", Role.ADMIN));
        }

        public List<Item> getAllItems() { return new ArrayList<>(items); }
        public List<Claim> getAllClaims() { return new ArrayList<>(claims); }

        public Item addLostItem(String name, String category, String location, String date, String ownerContact) {
            requireText(name, "Name");
            requireText(category, "Category");
            requireText(location, "Location");
            Item item = new LostItem(nextItemId(), name, category, location, defaultDate(date), ownerContact);
            items.add(item);
            storage.saveItems(items);
            return item;
        }

        public Item addFoundItem(String name, String category, String location, String date, String storedAt) {
            requireText(name, "Name");
            requireText(category, "Category");
            requireText(location, "Location");
            Item item = new FoundItem(nextItemId(), name, category, location, defaultDate(date), storedAt);
            items.add(item);
            storage.saveItems(items);
            return item;
        }

        public Item findItem(int id) {
            for (Item item : items) {
                if (item.getId() == id) return item;
            }
            throw new ItemNotFoundException("No item with id " + id);
        }

        public void deleteItem(int id, String adminId) {
            requireAdmin(adminId);
            items.remove(findItem(id));
            storage.saveItems(items);
        }

        public List<Item> search(String keyword) {
            String k = keyword.toLowerCase();
            List<Item> result = new ArrayList<>();
            for (Item item : items) {
                if (item.getName().toLowerCase().contains(k)
                        || item.getCategory().toLowerCase().contains(k)
                        || item.getLocation().toLowerCase().contains(k)) {
                    result.add(item);
                }
            }
            return result;
        }

        public List<Item> findMatches(int itemId) {
            Item item = findItem(itemId);
            List<Item> matches = new ArrayList<>();
            for (Item other : items) {
                boolean oppositeKind = !other.getKind().equals(item.getKind());
                boolean sameCategory = other.getCategory().equalsIgnoreCase(item.getCategory());
                if (oppositeKind && sameCategory && !other.isReturned()) matches.add(other);
            }
            return matches;
        }

        public Claim submitClaim(int itemId, String studentId, String proof) {
            Item item = findItem(itemId);
            if (item.isReturned()) throw new InvalidInputException("This item was already returned");
            requireText(studentId, "Student ID");
            requireText(proof, "Proof of ownership");
            Claim claim = new Claim(nextClaimId(), itemId, studentId, proof);
            claims.add(claim);
            storage.saveClaims(claims);
            return claim;
        }

        public Claim approveClaim(int claimId, String adminId) {
            requireAdmin(adminId);
            Claim claim = findClaim(claimId);
            if (claim.getStatus() != ClaimStatus.PENDING) {
                throw new InvalidInputException("This claim was already processed");
            }
            claim.approve();
            findItem(claim.getItemId()).markReturned();
            storage.saveClaims(claims);
            storage.saveItems(items);
            return claim;
        }

        private Claim findClaim(int id) {
            for (Claim c : claims) {
                if (c.getId() == id) return c;
            }
            throw new ItemNotFoundException("No claim with id " + id);
        }

        private void requireAdmin(String adminId) {
            User user = users.get(adminId);
            if (user == null || user.getRole() != Role.ADMIN) {
                throw new NotAuthorizedException("Only the Dean of Students office can do this");
            }
        }

        private void requireText(String value, String field) {
            if (value == null || value.isBlank()) throw new InvalidInputException(field + " is required");
        }

        private String defaultDate(String date) {
            return (date == null || date.isBlank()) ? LocalDate.now().toString() : date;
        }

        private int nextItemId() {
            return items.stream().mapToInt(Item::getId).max().orElse(0) + 1;
        }

        private int nextClaimId() {
            return claims.stream().mapToInt(Claim::getId).max().orElse(0) + 1;
        }
    }

