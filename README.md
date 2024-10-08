# Banking-system (Junit)

### Jpa LocalDateTime 자동으로 생성하는 법
- @EnableJpaAuditing(Main class)
- @EntityListeners(AuditingEntityListener.class) (Entity class)
```java
    @CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedAt;
```