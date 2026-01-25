package server;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import common.Token;

/**
 * TokenStore holds a list of Tokens, which could be shared among several
 * services in the distributed system.
 */
public class TokenStore {
    private static final TokenStore instance = new TokenStore();
    
    private List<Token> generatedTokens = new CopyOnWriteArrayList<>();

    private TokenStore() {
    }

    public static TokenStore getInstance() {
        return instance;
    }

    public void store(Token t) {
        generatedTokens.add(t);
    }

    public boolean isPresent(Token t) {
        return generatedTokens.contains(t);
    }

    public boolean isValid(String payload) {
        return generatedTokens.stream()
                .filter(t -> Instant.now().isBefore(t.endTime()))
                .anyMatch(t -> t.payload().equals(payload));
    }

    public boolean isValidSignature(String payload) {
        Optional<Token> o = generatedTokens.stream()
                .filter(t -> t.payload().equals(payload)).findAny();
        if (o.isEmpty())
            return false;
        return isValidSignature(o.get());
    }

    public boolean isValidSignature(Token token) {
        TokenGenerator tGen = TokenGenerator.getInstance();
        boolean verif = false;
        try {
            verif = tGen.verifySignature(token.signature(), token.payload());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return verif;
    }
}