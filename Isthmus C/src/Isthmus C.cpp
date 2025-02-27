//============================================================================
// Name        : Isthmus.cpp
// Author      : Yann-Gaël Guéhéneuc
// Version     : GPL v3
// Copyright   : Your copyright notice
// Description : Hello World in C++, Ansi-style
//============================================================================

#include <sys/errno.h>
#include <algorithm>
#include <cmath>
#include <iostream>
#include <string>
#include <cerrno>
#include <cstring>
#include <fstream>

using namespace std;

/* Declarations */
typedef int (*fp_max_comparator)(const int, const int);
void function_needing_some_comparator(const fp_max_comparator);

/* Definitions */
int my_max_function(const int a, const int b) {
	return a > b ? a : b;
}

int main() {
	function_needing_some_comparator(&my_max_function);

	// Declaring a boolean type array
	bool a[5];
	bool result;
	for (int i = 0; i < 5; ++i) {
		cout << a[i] << " ";
		result += a[i];
	}
	cout << endl << result << endl;

	// Declaring and changing strings
	string string1 = "Hello";
	string string2 = ", World!";

	string string3 = string1 + string2;
	cout << string3 << endl;

	string3[0] = 'Y';
	cout << string3 << endl;

	return 0;
}

void function_needing_some_comparator(const fp_max_comparator compartor) {
	cout << compartor(1, 2) << endl;
}

class Naming {
public:
	// int identifier = 0; // Can't compile with this declaration
	void identifier() {
	}
};

namespace a {
namespace b {
int x = 0;
}
}
namespace b {
string x;
}
using namespace a;
void foo() {
	// b::x = 42;
}

using namespace std;
void abssort(float *x, unsigned n) {
	sort(x, x + n, [](float a, float b) {
		return (abs(a) < abs(b));
	}
	);
}

int example_errno() {
	// Defining the filename to be opened.
	const char *filename = "myfile.txt";

	// Attempting to open the file using ifstream.
	ifstream file(filename);

	// Checking if the file was not opened successfully or
	// not.
	if (!file) {
		// Switch case based on the error number (errno).
		switch (errno) {
		case ENOENT: // Case for file not existing.
			cerr << "Error: File '" << filename << "' doesn't exist." << endl;
			break;
		case EACCES: // Case for access denied.
			cerr << "Error: Permission denied for file '" << filename << "'."
					<< endl;
			break;
		default: // Default case for other errors.
			cerr << "Error opening file '" << filename << "': "
					<< strerror(errno) << endl;
			break;
		}
		// Return with error code 1 to indicate failure.
		return 1;
	}

	// Closing the file if it was opened successfully.
	file.close();
	return 0;
}
