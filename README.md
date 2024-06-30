Artemis Financial: Modernizing Financial Operations:

Artemis Financial is a financial consulting company that needed to modernize its operations by using cutting-edge software and the best security practices available for their web application.

Project Background:

Upon investigation, I identified a number of outdated dependencies in their Spring Boot application. As Artemis deals with consumer information, it is legally required to maintain the highest security standards. The challenge was the usage of outdated software and dependencies that posed various security concerns. Filtering through false positives flagged by the Maven plugin required a significant amount of time and effort.

Security Enhancements:

After refactoring the code, I implemented a secure hashing algorithm (SHA-256) and an advanced encryption method (AES-GCM-256), which is secure against even quantum-level attacks. I also updated dependencies and implemented a security certificate for HTTPS/SSL protocols.

Post-Implementation Assessment:

After refactoring the code and updating dependencies, I ran the vulnerability assessment again to ensure no new vulnerabilities had been created or existed. These cryptography methods, alongside the certificate generation, are standard security procedures when dealing with sensitive information.

Key Takeaways:

From this assignment, the key takeaways are the cryptography methods and secure information handling practices that are vital to any business or institution. This project underscores the importance of maintaining up-to-date dependencies and implementing robust security measures, especially when handling sensitive consumer information.
