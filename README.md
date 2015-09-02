# LangDetect
Automatic Language Detection in Java

## Functionality

This is a straightforward implementation of language detecting using n-gram frequency profiles as described in:

> William B. Cavnar and John M. Trenkle. **N-Gram-Based Text Categorization**. In Proceedings of SDAIR-94, 3rd Annual Symposium on Document Analysis and Information Retrieval, 1994.

As training data, versions of the Universal Declaration of Human Rights in 9 european languages are used. All inputs are compared to the frequency profiles computed based on this training texts in order to identify the language.

## Usage

To start the program, download the full project and execute the following command in the main folder:

```java -cp bin langdetect.LangDetect```

During the start, the program reads all texts in the specified training data folder. Each text is assumed to represent a language and the file name is used to identify that language. To overwrite the default path (*data/training*), another path can be passed as a parameter:

```java -cp bin langdetect.LangDetect other_training_folder```

Once the frequency profiles for the training data are computed, the program prompts for queries. Text can be entered and is checked against to language profiles to identify its langauge. 
The output lookes like this

```>java -cp bin langdetect.LangDetect
Initializing...
Available languages: da, de, en, es, fi, fr, it, nl, pt

Query: (or type 'exit')
This is a sentence in english
 1. en (16264)
 2. es (17927)
 3. fr (18010)```
 
and shows the three best guesses, with the corresponding out-of-place-measure given in parenthesis.
 
The program can be terminated by typing *exit* as a query.

## Project Structure

The project is structured as follows:

* **src** source code
* **test** source code of unit tests
* **bin** binaries
* **data** training data
* **doc** generated java doc
* **lib** used libraries