# Lilith

Lilith is a logging and access event viewer for the Logback logging
framework, log4j and java.util.logging.


It has features comparable to Chainsaw, a logging event viewer for log4j.
This means that it can receive logging events from remote applications using
Logback as their logging backend.


It uses files to buffer the received events locally, so it is possible to
keep vast amounts of logging events at your fingertip while still being able
to check only the ones you are really interested in by using filtering
conditions.


Lilith V8.0.0 has been released on 2015-11-15!


Download it now at [SourceForge][ext-sf-files]


Are you running Mac OS X 10.8 and Lilith won't start?  
Read [this][osx]!


<a href="//twitter.com/lilithapp" class="twitter-follow-button" data-show-count="false">Follow @lilithapp</a><script async src="//platform.twitter.com/widgets.js" charset="utf-8"></script>
[![I use this!][ext-ohloh-btn]][ext-ohloh-prj] 
[![Flattr this][ext-flattr-btn]][ext-flattr-lnk]

### View the presentation now…

[![Presentation video on YouTube][ext-yt-thumb]][ext-yt-lnk]

Feel free to contact me at contact[at]lilith.huxhorn.de.


- - -

Starting with Lilith 0.9.39, all sulky and lilith artifacts are signed.
All previous releases have been signed with the same key in the git
repositories.

```
pub   4096R/740A1840 2010-03-25
      Key fingerprint = 3CEC E46C 577B 8BFE 85B6  5725 6334 E557 740A 1840
uid                  Joern Huxhorn <huxhorn [AT] users.sourceforge.net>
uid                  Joern Huxhorn <huxhorn [AT] users.sf.net>
uid                  Joern Huxhorn <joern [AT] lilith.huxhorn.de>
uid                  Joern Huxhorn <contact [AT] lilith.huxhorn.de>
sub   4096R/C2D09CF4 2010-03-25
```

- - -

[Screenshots][screenshots]

- - -

[![Open Hub project report for Lilith][ext-openhub-lilith-btn]][ext-openhub-lilith-lnk]  
[![Open Hub project report for sulky][ext-openhub-sulky-btn]][ext-openhub-sulky-lnk]

{% include footer.md %}

[osx]: osx.md
[screenshots]: screenshots.md


[ext-sf-files]: //sourceforge.net/projects/lilith/files/lilith/8.0.0

[ext-ohloh-btn]: media/ohlo-70x23.png
[ext-ohloh-prj]: //www.ohloh.net/stack_entries/new?project_id=lilith "Support Lilith by adding it to your stack at Ohloh"

[ext-openhub-lilith-lnk]: https://www.openhub.net/p/lilith?ref=Partner+Badge
[ext-openhub-lilith-btn]: //www.openhub.net/p/lilith/widgets/project_partner_badge?format=png&ref=Partner+Badge

[ext-openhub-sulky-lnk]: https://www.openhub.net/p/sulky?ref=Partner+Badge
[ext-openhub-sulky-btn]: //www.openhub.net/p/sulky/widgets/project_partner_badge?format=png&ref=Partner+Badge

[ext-flattr-btn]: media/flattr-100x17.png
[ext-flattr-lnk]: //flattr.com/thing/15170/Lilith-Logging-and-AccessEvent-Monitor-for-Logback "Flattr this"

[ext-yt-thumb]: //img.youtube.com/vi/R-VRDqMQwAg/0.jpg
[ext-yt-lnk]: //www.youtube.com/watch?v=R-VRDqMQwAg
