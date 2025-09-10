How to run Project

1. clone the github repository

	git clone https://github.com/JaiderVargas05/AREP-Parcial1.git
	
2. Compile and install dependencies

	cd Arep-Parcial1
	
	mvn clean install
3. Run backend
	java -cp target/classes escuelaing.edu.co.arep.parcial1.HttpServer

4. Run Facade
	java -cp target/classes escuelaing.edu.co.arep.parcial1.Facade