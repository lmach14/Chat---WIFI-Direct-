პლატიაგში რომ არ ჩაგვეთვალოს ვიყენებდით android.googlesource.com ტუტორიალს:https://android.googlesource.com/platform/development/+/master/samples/WiFiDirectServiceDiscovery?autodive=0%2F

ეს ფაილი გამოგვადა, მაგრამ იყო საკმაოდ ბევრი საკითხი მაგ. სოკეტების ქონექშენის კონტროლი და ასევე WIFI Direct-ს საკითხები რომელსაც ვერ წყვეტდა კარგად აღნიშნული ტუტორიალი და ჩვენ გავაუმჯობესეთ

დავიწყოთ wifi direct ით:

გამოვიყენეთ Wi-Fi Direct (P2P) for service discovery, რათა peerები გვეპოვა მათი wifi service-ის პოვნის საშუალებით.

ლოგიკა არის შემდეგი: ჯერ ყოველთვის ვამოწმებთ და ვშლით არის თუ არა უკვე დაყენებეული რამე ლოკალური სერვისი ან სერვისზე მოთხოვნა, თუ არის ვშლით და ახალ მოთხოვნას ვარეგისტირებთ.

თუ სერვისი იპოვნა და დაუკავშირდა, დაკავშირებას უკვე იჭერს ბროდქასთლისენერი, და იხმობს შესაბამის მეთოდს

ერთი დეტალიც გავითვალისწინეთ, როცა ერთმა დივაისმა დაინახა და დაკავშირება მოითოვა როცა მეორე ვერ ხედავდა. ამ დროს იმ დივაისში რომელიც ვერ ხედავს მეორე, დივაისს ქონექშენის დროს არ აქვს

ნაპოვში მეორე ტელეფონის ინფორმაცია, ამიტომ ვიჭერთ ამ შემთხვევას და "onGroupInfoAvailable" ლისენერის საშუალებით დაქონექთების მომენტში ვიჭეღთ ინფორამციას მეორე დივაისისას

ასევე აუცილებელი იყო დაქრაშვის ან უკან გადასვლის დროს Wifi Direct ის გათიშვა დაკონექთებულ დივაისებს შორის

Socket Connection:

3 კლასი:
1 ესაა სერვერ სოკეტის კლასი
2 ესაა კლიენტ სოკეტის კლასი
3 წამკითხველი და გამგზავნი დამხარე კლასი რომელიც ორივე აღნიშნული კასს ჭირდება ურთიერთობისთვის

ამ შეთხვევაში რადგან ეს ყველაფერი ცალ-ცალკე სრედებში ეშვება, საჭიროა კომუნიკაციის მექანიზმი რაცაა interupt მეთოდი: თუ ერთი სრედის მიერ მეორეს გაჩერება დაგჭირდა.

ასევე კლიენტ და სერვერ სოკეტების დახურვის აუცილებლობა როცა კლინტი გადის აპლიკაციიდან ან დაქრაშვა ხდება, serversokets ს ხურავს წამკითხველი და გამგზავნი კლასი, რადგან ის

ხვდება პირველი კლიენტის მოქმედებას.

