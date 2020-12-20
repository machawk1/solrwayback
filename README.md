# SolrWayback

## SolrWayback 4.0.6 software bundle has been released
SolrWayback bundle release for 4.0.6 here: https://github.com/netarchivesuite/solrwayback/releases/tag/4.0.6

## About SolrWayback

SolrWayback is web-application for browsing historical harvested ARC/WARC files similar
to the Internet Archive Wayback Machine. The SolrWayback uses on a Solr server with Arc/Warc files indexed using the warc-indexer.

SolrWayback comes with additional features:
* Free text search in all resources (HTML pages, pdf's, meta data for different media types, urls etc.)
* Interactive link graph (ingoing/outgoing) for domains.
* Export of search results to a Warc-file. Streaming download, no limit of size of resultset.
* CSV text export of searc result with custom field selection.
* Wordcloud generation for domain
* N-gram search visuzalisation
* Visualization of search result by domain.
* Visualization of various domain statistics over time such as size, number of in and out going links.
* Large scale export of linkgraph in Gephi format. (See https://labs.statsbiblioteket.dk/linkgraph/ )
* Image search similar to google images
* Search by uploading a file. (image/pdf etc.) See if the resource has been harvested and find HTML pages using the image..
* View all fields indexed for a resource and show warc-header for records.
* Configure alternative playback engine to any playback engine using the playback-API such as Open Wayback or PyWb etc.


## Live demo
The National Széchényi Library of Hungary has kindly set up the following demo site for SolrWayback <br>
http://webadmin.oszk.hu/solrwayback/


## See also
Warc-indexer: https://github.com/ukwa/webarchive-discovery/tree/master/warc-indexer<br>
The Warc indexer is the indexing engine for documents in SolrWayback. It is maintained by the British Library.

Netsearch(Archon/Arctika): https://github.com/netarchivesuite/netsearch<br>
Archon/Actika is a book keeping application for warc-files and can start multiple concurrent warc-indexer jobs for large scale netarchives. 

## SolrWayback Screenshots

<p align="center"> 
   <img src="https://github.com/netarchivesuite/solrwayback/blob/master/doc/solrwayback_search.png?raw=true" />
</p>
<p align="center">
 Lising of search results with facets.
</p>

<p align="center"> 
   <img src="https://github.com/netarchivesuite/solrwayback/blob/master/doc/image_search.png?raw=true"/>
</p>
<p align="center">
  Image search, show only images as results.
</p>

<p align="center"> 
   <img src="https://github.com/netarchivesuite/solrwayback/blob/master/doc/solrwayback_playback.png?raw=true" />
</p>
<p align="center">
Solrwayback showing the playback of an archived webpage with playback toolbox overlay.
</p>

<p align="center"> 
   <img src="https://github.com/netarchivesuite/solrwayback/blob/master/doc/solrwayback_linkgraph.png?raw=true" />
</p>
<p align="center">
Interactive domain link graph
</p>

<p align="center"> 
   <img src="https://github.com/netarchivesuite/solrwayback/blob/master/doc/solrwayback_crawltimes.png?raw=true" />
</p>
<p align="center">
Github like visualization of crawltimes
</p>


<p align="center"> 
   <img src="https://github.com/netarchivesuite/solrwayback/blob/master/doc/gps_exif_search.png?raw=true" />
</p>
<p align="center">
Search in images by gps location in images having exif location information about the location.
</p>

<p align="center"> 
   <img src="https://github.com/netarchivesuite/solrwayback/blob/master/doc/solrwayback_wordcloud.png?raw=true" />
</p>
<p align="center">
Generate a wordcloud for a domain
</p>

<p align="center"> 
   <img src="https://github.com/netarchivesuite/solrwayback/blob/master/doc/solrwayback_ngram.png?raw=true" />
</p>
<p align="center">
n-gram visualization of results by year, relative to the number of results that year.
</p>


## API
The API for linking to and browsing archived webpages is the same as for Internet Archive:<br>

Internet Archive:https://web.archive.org/web/20080213093319/http://www.statsbiblioteket.dk/ <br>
SolrWayback: http://server/solrwayback/services/web/20140515140841/http://statsbiblioteket.dk/ <br>
 

## Improving playback with the SolrWayback Root servlet.
Installing the root-servlet will improve playback of sites that are leaking urls. The root-servlet will
catch relative leaks (same domain) even without using proxy mode. The leaks will then be redirected back into SolrWayback to the correct URL and crawltime.
The root-servlet is included in the bundle install. In Tomcat it must be named ROOT.war.
Link to SolrWayback root proxy:
https://github.com/netarchivesuite/solrwaybackrootproxy
Absolute URL live-leaks (starting with http://domain...) will not be caught and can leak to the open web. Open network (F12) to see if any resources are leaking, or turn-off the internet connection to be sure there is no live leaks during playback.

 
 
## Requirements
 * Works on MacOs/Linux/Windows.  
 * JDK 8/9/10/11 
 * A nice collection of Arc/Warc files or harvest your own with Heritrix,Webrecorder,Brozzler, Wget etc. 
 * Tomcat 8+  or another J2EE server for deploying the WAR-file
 * A Solr 7.X server with the index build from the Arc/Warc files using the Warc-Indexer version 3.2.0-SNAPSHOT +
 * (Optional) chrome/(chromium) installed for page previews to work. (headless chrome) 
 
## Build and usage
 * Build the application with: `mvn package`
 * Deploy the `target/solrwayback-*.war` file in a web-container
 * Copy `src/test/resources/properties/solrwayback.properties` and `/src/test/resources/properties/solrwaybackweb.properties`
   to `user/home/` folder for the J2EE server
 * Modify the property files. (default all urls http://localhost:8080)
 * Open search interface: http://localhost:8080/solrwayback

## Contact
Thomas Egense (thomas.egense@gmail.com) 
Feel free to send emails with comments or questions.

# SolrWayback software bundle 4.0 install guide

With this download you will be able to index, search and playback web pages from your warc-files.
The bundle contains Solr, the warc-indexer tool and Solrwayback installed on a Tomcat webserver.
Just unzip the bundle and copy two files to your home directory and explore your warc files. 

## Download
Download : https://github.com/netarchivesuite/solrwayback/releases/download/4.0.6/solrwayback_package.zip  
Unzip and follow the instructions below.
 



## Install instructions:

### 1) Upgrade from 3.x

To update from 3.x add the new additional properties in `solrwaybackweb.properties` and `solrwayback.properties`. Download the release and to see the new properties. Some properties has been removed or renamed.
Replace both war-file in tomcat with this those in this release(solrwayback.war+ROOT.WAR) and restart tomcat.

### 1) INITIAL SETUP  
Properties:  
Copy the two files `src/test/resources/properties/solrwayback.properties` and `/src/test/resources/properties/solrwaybackweb.properties` to your HOME folder (or the home-folder for Tomcat user)

Optional: For screenshot previews to work you may have to edit `solrwayback.properties` and change the value of the last two properties : `chrome.command`  and `screenshot.temp.imagedir`. 
Chrome(Chromium) must has to be installed for screenshot preview images.  

If there are errors when running a script, try change the permissions for the file (`startup.sh` etc). Linux: `chmod +x filename.sh`

### 2) STARTING SOLRWAYBACK  
Solrwayback requires both Solr and Tomcat to be running. 

Tomcat:  
Start tomcat: `apache-tomcat-8.5.60/bin/startup.sh`  
Stop tomcat:  `apache-tomcat-8.5.60/bin/shutdown.sh`  
(For windows navigate to `apache-tomcat-8.5.60/bin/` and type `startup.bat` or `shutdown.bat`)  
To see Tomcat is running open: http://localhost:8080/solrwayback/  
  
Solr:  
Start solr: `solrwayback_package/solr-7.7.3/bin/solr start`  
Stop solr: `solrwayback_package/solr-7.7.3/bin/solr stop -all`  
(For windows navigate to `solrwayback_package/solr-7.7.3/bin/` and type `solr.cmd start` or `solr.cmd stop -all`)    
To see Solr is running open: http://localhost:8983/solr/#/netarchivebuilder  

### 3) INDEXING
Solrwayback uses a Solr index of warc files to support freetext search and more complex queries.  
If you do not have existing warc files, see steps below on harvesting with wget.        

Copy arc/warc files into folder: `/solrwayback_package/indexing/warcs1`  
Start indexing:  call `indexing/batch_warcs1_folder.sh` (or batch_warcs1_folder.bat for windows)
Indexing can take up to 20 minutes for 1GB warc-files. After indexing, the warc-files must stay in the same folder since SolrWayback is using them during playback etc.  
Having whitespace characters in warc file names can result in pagepreviews and playback not working on some OS.
There can be up to 5 minutes delay before the indexed files are visible from search. Visit this url after index job have finished to commit them instantly: http://localhost:8983/solr/netarchivebuilder/update?commit=true  
There is a batch_warcs2_folder.sh similar script to show how to easy add new warc-files to the collection without indexing the old ones again.
Or you can use the command in the batch_warcs2_folder.sh(bat) to see how to just index a single warc-file with the warc-indexer java command. 

Deleting an Index:  
If you want to index a new collection into solr and remove the old index.  
1) stop solr  
2) delete the folder:   
`solr-7.7.3/server/solr/netarchivebuilder/netarchivebuilder_data/index`  
(or rename to `index1` etc, if you want to switch back later)  
3) start solr  
4) start the indexing script. 

Faster indexing:  
A powerful laptop can handle up to 6 simultaneous indexing processes with Solr running on the same laptop. 
Using an SSD for the Solr-index will speed up indexing and also improve search/playback performance.

Windows only Solrwayback control GUI:
For windows users there is a executable GUI set-up program that will start tomcat/solr and copy properties to the home-directory.
From the GUI you can select warc-files with a file choose and start indexing. Click the /addOn/SolrSetup.exe file to start GUI.
For more information see: https://github.com/MadsGreen/SolrSetup/

### 4) SEARCHING AND ADDITIONAL FEATURES  
Click the question mark in the search-field to get help with the search syntax for more complex queries and using 
field queries.
The toolbar icon opens a menu with the available tools.


### 5) TO CREATE YOUR OWN WARCS - HARVESTING WITH WGET  
How to do your own web harvest websites (Linux/MacOS only):  
Using the wget command is an easy way to harvest websites and build warc-files. The warc-files can then be indexed into SolrWayback.  
Create a new folder since there will be several files written in this folder. Navigate to that folder in a prompt.  
Create a text file call `url_list.txt` with one URL pr. line in that folder.  
Type the following in a prompt:  
`wget  --span-hosts  --level=0 --recursive --warc-cdx   --page-requisites --warc-file=warcfilename --warc-max-size=1G -i url_list.txt`    
  
The script will harvest all pages in the `url_list.txt` file with all resources required for that page (images, css etc.) and be written to a warc file(s) called `warcfilename.warc`  
Change `--level=0` to `--level=1` for following links. This will substantially increase the size of the warc file(s).  
The optional  --span-hosts parameter will also harvest resources outside the domain of the page and can be removed 


