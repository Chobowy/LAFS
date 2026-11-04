# ZUT Campus Lost & Found (Back2u Crew)

## The problem
Students at ZUT lose IDs, NRCs, phones and keys every week. Posts on WhatsApp
statuses disappear after 24 hours, and the Dean of Students Office has no system
to match owners. Students pay K100-K250 to replace IDs that were already found.

## Users
- Student who lost an item
- Student who found an item
- Dean of Students office (approves claims)

## How to run
1. Install JDK 17 or newer and IntelliJ IDEA.
2. Open the project folder in IntelliJ and let Maven load.
3. Run zut.lafs.Main.
4. Open http://localhost:7070/ in a browser.
Admin ID for approving claims: ADMIN01

## Classes
- Item (abstract), LostItem, FoundItem: items and their shared details
- User, Role: students and the admin
- Claim, ClaimStatus: a claim with proof and status
- ItemService: holds the lists and the logic (search, matches, claims)
- FileStorage: saves and loads data as JSON in the data folder
- ItemNotFoundException, InvalidInputException, NotAuthorizedException: custom exceptions
- Main: Javalin REST endpoints and static front end

## Team
Sante Kajoba (2410240), Choolwe Chanda (2410255), Chilufya Konkola (2410057),
Evaristo Kabwe (2410417), Andrew Ngalasa (2410233)
