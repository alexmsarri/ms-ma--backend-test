## Important
- IDE requires **Lombok** in order to compile. Check https://projectlombok.org/setup/eclipse.
- The API requires HTTP Basic Authentication. First user can be created anonymously or using 'admin' / 'password' credentials.
- There's an Integration Test *FriendsIT* with the Test Cases.

## Frameworks and Libraries used:
  - Spring Boot.
  - Spring Data.
  - Spring Security.
  - Swagger2 (http://URI:PORT/swagger-ui.html admin / password).
  - Lombok.
  - ModelMapper.

## Missing features:
  - Remove a friend.
  - More Tests (Controller and Service Layer).


---------------------------------------------------
# Friends
*(If you first thought on the TV comedy, you are getting old)*

We are going to make a Social Network MVP. **No frontend. Just an API.**

We will have users that can friend other users.

## Specs

Users will only have:
 - Username (unique) 5-10 characters, ONLY alphanumeric characters (no dots no commas no underscores)
 - Sign up date set on creation
 - Profile visibility:
   - Hidden: Cannot receive friendship requests
   - Public: Can receive friendship requests **(DEFAULT: When signup)**
 - Password 8-12 characters, ONLY alphanumeric characters (no dots no commas no underscores)

We need the following use cases:
 - Sign up user
 - Request friendship (Ensure users requesting, accepting or declining friendships are theirselves)
    - Accept friendship
    - Decline friendship (when a user gets 3 declines that user gets a forever alone **badge**)
 - Get user friends

## Tech specs

### Required
 - Good OOP and good practices. This is the most important.
 - Java *(or Kotlin. If you choose Kotlin over Java be pretty sure what you do. REMEMBER: We value good OOP and good practices.)*
 - No frontend
 - API
 - Use (or not) whatever framework you want
 - Do your best. Be proud of what you did. That's it.
 
### Not needed
 - Database: You can persist everything in memory.
 - Docker: as we do not require using database or caches. We will run the application directly in local.
 - Deploy
 - Asynchrony (Neither futures nor reactive)

### Preferred but not mandatory
**DON'T WORRY ABOUT THIS, IF YOU FEEL MORE COMFORTABLE WITH OTHER FRAMEWORK OR BUILD SYSTEMS USE THEM. SERIOUSLY**
 - Gradle over Maven
 - Spring over other frameworks
 
### Notes
If you are going to use Authorization headers, use Basic Auth
