mkdir Fastest
mkdir Fastest/doc
mkdir Fastest/lib
mkdir Fastest/temp
mkdir Fastest/lib/conf
scp fastest.jar Fastest
scp fastest-server.jar Fastest
scp lib/antlr-3.2.jar Fastest/lib
scp lib/czt.jar Fastest/lib
scp lib/jline-0.9.94.jar Fastest/lib
scp lib/ZCharMap.jar Fastest/lib
scp lib/ZEvesPlugin.jar Fastest/lib
scp lib/ZLivePlug2in.jar Fastest/lib
scp lib/ZLivePlugin.jar Fastest/lib
scp lib/ZSideKick.jar Fastest/lib
scp lib/conf/cserversinfo.conf Fastest/lib/conf
scp lib/conf/elimTheorems.tex Fastest/lib/conf
scp lib/conf/fastest.conf Fastest/lib/conf
scp lib/conf/listoftactics.conf Fastest/lib/conf
scp lib/conf/rwRules.tex Fastest/lib/conf
scp lib/conf/server-port.conf Fastest/lib/conf
scp lib/conf/stdpartition.spf Fastest/lib/conf
scp lib/conf/thmoperators.conf Fastest/lib/conf
scp doc/sensors-simp.tex Fastest/doc
tar -czf fastest.tar.gz Fastest
rm -r -f Fastest
