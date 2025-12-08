package usuario;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.time.LocalDateTime;

@Document(collection = "chats")
public class Chat {
    @Id
    private String id;
    private List<String> participants; // Nombres de usuario, no IDs
    private String lastMessage;
    private LocalDateTime lastMessageDate;

    private String productId;
    private String productName;
    private String productImage;

    public enum Status {
        ACTIVE,
        WAITING_SEAL_CONFIRMATION,
        SEALED,
        ARCHIVED
    }

    private Status status = Status.ACTIVE;
    private String dealInitiator;
    private boolean buyerRated;
    private boolean sellerRated;

    public Chat() {
    }

    public Chat(List<String> participants, String productId, String productName, String productImage) {
        this.participants = participants;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.lastMessageDate = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public LocalDateTime getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(LocalDateTime lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDealInitiator() {
        return dealInitiator;
    }

    public void setDealInitiator(String dealInitiator) {
        this.dealInitiator = dealInitiator;
    }

    public boolean isBuyerRated() {
        return buyerRated;
    }

    public void setBuyerRated(boolean buyerRated) {
        this.buyerRated = buyerRated;
    }

    public boolean isSellerRated() {
        return sellerRated;
    }

    public void setSellerRated(boolean sellerRated) {
        this.sellerRated = sellerRated;
    }
}
