# InternetApplications Project 2020
Στο πλαίσιο του μαθήματος Διαδίκτυο και Εφαρμογές αναπτύχθηκε η εφαρμογή IP2Weather που επιτρέπει στο χρήστη
να ανακτά τις καιρικές συνθήκες που επικρατούν στην περιοχή ενός host μηχανήματος βάση της IP του.
Ο χρήστης δίνει ως είσοδο μια IP διεύθυνση και η εφαρμογή αναζητά πληροφορίες σχετικά με την τοποθεσία καθώς
και τις καιρικές συνθήκες που επικρατούν σε αυτή, τις οποίες και επιστρέφει στο χρήστη.

Η εφαρμογή αποτελείται από μία βάση δεδομένων στην αποία γίνεται caching των παραπάνω πληροφοριών μειώνοντας τις αναφορές
στα web services, ένα back-end/REST API υπεύθυνο για την ανάκτηση τους από τα web APIs ή τη βάση δεδομένων αν υπάρχουν
ήδη εκεί, καθώς και τον έλεγχο της εγκύτητάς τους φροντίζοντας την ανανέωσή τους στη βάση αν έχει περάσει κάποιo
χρονικό διάστημα από το τελευταίο refresh τους (κάθε αναφορά στις υπηρεσίες ή στη βάσης γίνεται μόνο on-demand της πληροφορίας).

Έχει μόλις ένα end-point για http get requests που φέρουν και την ip, ενώ παρέχει δυνατότητα επιλογής του response format
μεταξέ json και xml (από default χρησιμοποιείται json).

Επιπλέον αναπτύχθηκε μια single page σελίδα ως front-end που επιτρέπει τη γραφική παρουσίαση των αποτελεσμάτων, μαζί με την ύπαρξη χάρτη όπου προβάλεται η τοποθεσία που ανακτάται από το API, ενώ η εισαγωγή της IP γίνεται μέσω html input form.
Σε περίπτβωση που η φόρμα δήλωσης αφεθεί κενή από το χρήστη γίνεται αυτόματα αναζήτηση της IP αυτού μέσω του API
http://api.ipify.org/?format=json ενώ ακολουθείται αντίστοιχη διαδικασία με πριν για τα υπόλοιπα.


# APIs used:

Αντιστοίχιση ip σε τοποθεσία (ip geolocation)\
https://freegeoip.app/json/{ip}

Καιρικές συνθήκες σε συγκεκριμένη τοποθεσία βάση ονόματος πόλης και γεωγραφικών συντεταγμένων αντίστοιχα\
http://api.openweathermap.org/data/2.5/weather?q={city_name}&appid={api.key} \
http://api.openweathermap.org/data/2.5/lat={latitude}&lon={longitude}%s&appid={api.key}

Εύρεση της IP του χρήστη (client-side)\
http://api.ipify.org/?format=json


# Technologies:

front-end: HTML, CSS, JavaScript (AJAX and leaflet)\
back-end: JAVA (Spring framework)\
database: MongoDB

# Required Instalations:

jdk-14.0.2\
apache-maven-3.6.3\
mongodb-4.2.8\
(οποιοδήποτε http-server για το front-end που να υποστηρίζει HTML5)

# Deployment:

Αρχικά πρέπει κανείς να κατεβάσει τα αρχεία από το repository
(περιβλάλλον υλοποίησης Windows 10, σε περίπτωση λειτουργικού τύπου Unix
απαιτείται αντικατάσταση των backslash με slash στα ακόλουθα βήματα)

Εκκίνηση mongodb για πρώτη φορά (default port->27017):\
cd {path\to\installation\dir}\MongoDB\Server\4.2\bin\
mkdir {database\path\folder}\
mongod --dbpath="{database\path\folder}"\
(Οι παραπάνω εντολές εκετελούνται κατά την πρώτη εκκίνηση της βάσης, ώστε να οριστεί το directory αποθήκευσης των αρχείων που θα δημιουργηθούν, προτίνεται η χρήση ενός νέου directory που δημιουργεί ο χρήστης για αποφυγεί περιορισμένων δικαιωμάτων πρόσβασης της mongodb σε αυτό)

Εκκίνηση REST API (back-end τρέχει στη 8080 θύρα):\
cd {path\to\downloaded\project}\back-end\rest-service
mvn spring-boot:run

Εκκίνηση front-end:\
Εξαρτάται από τον http-server της επιλογής σας, ή άνοιγμα με browser (δοκιμάστηκε επιτυχώς σε monzilla firefox).\
Κατόπιν εκκίνησης αρκεί σύνδεση μέσω browser στη localhost:port
όπου port η θύρα στην οποία τρέχει ο http server.

# Use instructions and functionalities:

Χρήση γραφικού περιβάλλοντος front-end (functionalities):\
Η σελίδα φέρει μία φόρμα συμπλήρωσης της επιθυμητής IP διεύθυνσης χωρισμένη σε 4 πεδία για εύκολη εισαγωγή
αλλά και έλεγχο εγκυρότητας της εισόδου από τον χρήστη. Η υποβολή της IP για ανάκτηση της πληροφορίας γίνεται
μετά με το κουμπί submit (ενώ υπάρχει δυνατότητα αφεθεί κενή η φόρμα όπως περιγράφηκε ήδη).\
Στα δεξιά υπάρχει ένας interactive παγκόσμιος χάρτης για εμφάνιση της τοποθεσίας. Στην άνω δεξιά γωνία του
τοποθετήθηκε ένα κουμπί reset που υλοποιήθηκε για την εύκολη επαναφορά του χάρτη στο παρόν σημείο ενδιαφέροντος.
Ο χάρτης αυτός προστέθηκε με τη βοήθεια της βιβλιοθήκης leaflet σε JavaScript.

Χρήση REST API (back-end):\
Είναι εφικτή και η χρήση του REST API ως μια stand-alone εφαρμογή η οποία δέχεται http get requests της
μορφής http://localhost:8080/ip2weather?ip=2.2.2.2&mediaType=xml που θα επιστρέφει την πληροφορία σε xml
format αν οριστεί η mediaType μεταβήτή όπως φαίνεται στο url, αλλιώς αν οριστεί json ή δεν συμπεριληφθεί καθόλου
το response θα είναι σε json format. Η ip μεταβλητή είναι υποχρεωτική.


Σε κάθε περίπτωση από τις παραπάνω έχει υλοποιηθεί κατάλληλος έλεγχος παραμέτρων καθώς και αiτημάτων με κατάλληλο
error checking και exception handling (τόσο σε back-end όσο και front-end) με επιστροφή των κατάληλων μηνυμάτων
καθοδήγησης του χρήστη.

# Video link:

https://youtu.be/DP6kWuXwtY0

# Author:

Σταματελόπουλος Νικόλαος\
03116138
