package hu.kits.wallet.domain;

public record File(String id) {

    public boolean isPhoto() {
        return id.endsWith("png") || id.endsWith("jpg");
    }
    
}
