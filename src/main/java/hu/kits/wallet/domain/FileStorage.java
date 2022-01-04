package hu.kits.wallet.domain;

public interface FileStorage {

    byte[] getFile(String fileId);
    
    void saveFile(String fileId, byte[] bytes);
    
    void delete(String id);
    
    public static class DummyFileStorage implements FileStorage {

        @Override
        public byte[] getFile(String fileId) {
            return new byte[0];
        }

        @Override
        public void saveFile(String fileId, byte[] bytes) {
            
        }

        @Override
        public void delete(String id) {
        }
        
    }
    
}
