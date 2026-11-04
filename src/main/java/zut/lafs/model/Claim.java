package zut.lafs.model;

public class Claim {
    private int id;
    private int itemId;
    private String claimantStudentId;
    private String proof;
    private ClaimStatus status;

    protected Claim() { }

    public Claim(int id, int itemId, String claimantStudentId, String proof) {
        this.id = id;
        this.itemId = itemId;
        this.claimantStudentId = claimantStudentId;
        this.proof = proof;
        this.status = ClaimStatus.PENDING;
    }

    public int getId() { return id; }
    public int getItemId() { return itemId; }
    public String getClaimantStudentId() { return claimantStudentId; }
    public String getProof() { return proof; }
    public ClaimStatus getStatus() { return status; }

    public void approve() { this.status = ClaimStatus.APPROVED; }
    public void reject() { this.status = ClaimStatus.REJECTED; }
}
