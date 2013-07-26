package geogebra.html5.util;

import geogebra.common.main.App;
import geogebra.html5.Browser;
import geogebra.html5.main.AppWeb;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;

public class View {
	
	private HashMap<String, String> archiveContent;
	
	private Element container;
	private AppWeb app;
	
	public View(Element container, AppWeb app) {
		this.app = app;
		this.container = container;
    }

	public Element getContainer() {
	    return container;
    }

	public void promptUserForGgbFile() {		
		App.debug("No data-param-fileName attribute presented");
    }

	public void showError(String errorMessage) {
		App.debug(errorMessage);    
    }

	public void hide() {
		App.debug("View.hide called");
    }

	public void showLoadAnimation() {
	  	app.showLoadingAnimation(true); 
    }

	public String getDataParamBase64String() {
		return ((ArticleElement) container).getDataParamBase64String();
	}
	
	public String getDataParamLanguage() {
		return ((ArticleElement) container).getDataParamLanguage();
    }

	public String getDataParamCountry() {
		return ((ArticleElement) container).getDataParamCountry();
    }
	
	public boolean getDataParamUseBrowserForJS() {
		return ((ArticleElement) container).getDataParamUseBrowserForJS();
    }
	
	public boolean getDataParamEnableLabelDrags() {
		return ((ArticleElement) container).getDataParamEnableLabelDrags();
	}
	
	public boolean getDataParamEnableRightClick() {
		return ((ArticleElement) container).getDataParamEnableRightClick();
	}
	
	public boolean getDataParamShowMenuBar() {
		return ((ArticleElement) container).getDataParamShowMenuBar();
    }

	public boolean getDataParamShowAlgebraInput() {
		return ((ArticleElement) container).getDataParamShowAlgebraInput();
    }

	public boolean getDataParamShowToolBar() {
		return ((ArticleElement) container).getDataParamShowToolBar();
    }

	public boolean getDataParamShowToolBarHelp() {
		//return ((ArticleElement) container).getDataParamShowToolBarHelp();
		return false;
    }

	public boolean getDataParamShiftDragZoomEnabled() {
		return ((ArticleElement) container).getDataParamShiftDragZoomEnabled();
    }

	public boolean getDataParamShowResetIcon() {
		return ((ArticleElement) container).getDataParamShowResetIcon();
    }
	
	public boolean getDataParamShowAnimationButton() {
		return ((ArticleElement) container).getDataParamShowAnimationButton();
    }	
	
	private void maybeLoadFile() {
		if (app == null || archiveContent == null) {
			return;
		}

		try {
			app.loadGgbFile(archiveContent);
		} catch (Exception ex) {
			App.debug(ex.getMessage());
			return;
		}
		archiveContent = null;

		// app.getScriptManager().ggbOnInit(); //this line is moved from here too,
												// it should load after the images are loaded

		App.debug("file loaded");
		//This is used also by touch where dialog manager is null
		app.notifyFileLoaded();
		
		//reiniting of navigation bar, to show the correct numbers on the label
		if(app.getGuiManager() != null && app.getUseFullGui()){
			((this.getApplication().getGuiManager().getConstructionProtocolView())).getData().initView();
			this.getApplication().getConstructionProtocolNavigation().update();
		}
		
	}

	public void maybeLoadFile(HashMap<String, String> archiveCont) {
	    archiveContent = archiveCont;
	    maybeLoadFile();
    }

	public AppWeb getApplication() {
	    return app;
    }

	public void processBase64String(String dataParamBase64String) {
		archiveContent = new HashMap<String, String>();
		String workerUrls = (!Browser.webWorkerSupported ? "false" : GWT.getModuleBaseURL()+"js/zipjs/");
		populateArchiveContent(dataParamBase64String, workerUrls,this);
    }
	
	private int zippedLength = 0;
	
	private void putIntoArciveContent(String key, String value) {
		archiveContent.put(key, value);
		if (archiveContent.size() == zippedLength) {
			maybeLoadFile();
		}
	}

	private native void populateArchiveContent(String dpb64str, String workerUrls, View view) /*-{
		
		
		
		// Writer for ASCII strings
		function ASCIIWriter() {
			var that = this, data;
		
			function init(callback, onerror) {
				data = "";
				callback();
			}
		
			function writeUint8Array(array, callback, onerror) {
				var i;
				for (i = 0; i < array.length; i++)
					data += $wnd.String.fromCharCode(array[i]);
				callback();
			}
			
			function getData(callback) {		
				callback(data);
			}
		
			that.init = init;
			that.writeUint8Array = writeUint8Array;
			that.getData = getData;
		}
		ASCIIWriter.prototype = new $wnd.zip.Writer();
		ASCIIWriter.prototype.constructor = ASCIIWriter;
		
		function decodeUTF8(str_data) {
			var tmp_arr = [], i = 0, ac = 0, c1 = 0, c2 = 0, c3 = 0;
		
			str_data += '';
		
			while (i < str_data.length) {
				c1 = str_data.charCodeAt(i);
				if (c1 < 128) {
					tmp_arr[ac++] = String.fromCharCode(c1);
					i++;
				} else if (c1 > 191 && c1 < 224) {
					c2 = str_data.charCodeAt(i + 1);
					tmp_arr[ac++] = String.fromCharCode(((c1 & 31) << 6) | (c2 & 63));
					i += 2;
				} else {
					c2 = str_data.charCodeAt(i + 1);
					c3 = str_data.charCodeAt(i + 2);
					tmp_arr[ac++] = String.fromCharCode(((c1 & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
					i += 3;
				}
			}
	
			return tmp_arr.join('');
		}		
		
	    var imageRegex = /\.(png|jpg|jpeg|gif|bmp)$/i;
	    if (workerUrls === "false") {
	    	$wnd.zip.useWebWorkers = false;
	    } else {
    		$wnd.zip.workerScriptsPath = workerUrls;
	    }
	    
	    $wnd.zip.createReader(new $wnd.zip.Data64URIReader(dpb64str),function(reader) {
	        reader.getEntries(function(entries) {
	        	view.@geogebra.html5.util.View::zippedLength = entries.length;
	            for (var i = 0, l = entries.length; i < l; i++) {
	            	(function(entry){
		            	var filename = entry.filename;
		                if (entry.filename.match(imageRegex)) {
		                        @geogebra.common.main.App::debug(Ljava/lang/String;)(filename+" : image");
		                        entry.getData(new $wnd.zip.Data64URIWriter("image/"+filename.split(".")[1]), function (data) {
		                            view.@geogebra.html5.util.View::putIntoArciveContent(Ljava/lang/String;Ljava/lang/String;)(filename,data);
		                        });
		                    } else {
		                        @geogebra.common.main.App::debug(Ljava/lang/String;)(entry.filename+" : text");
		                        if ($wnd.zip.useWebWorkers === false || (typeof $wnd.zip.forceDataURIWriter !== "undefined" && $wnd.zip.forceDataURIWriter === true)) {
		                        	@geogebra.common.main.App::debug(Ljava/lang/String;)("no worker of forced dataURIWriter");
			                        entry.getData(new $wnd.zip.Data64URIWriter("text/plain"), function(data) {
			                			var decoded = $wnd.atob(data.substr(data.indexOf(",")+1));
			                          	view.@geogebra.html5.util.View::putIntoArciveContent(Ljava/lang/String;Ljava/lang/String;)(filename,decodeUTF8(decoded));
			                         });
		                        } else {
		                        	@geogebra.common.main.App::debug(Ljava/lang/String;)("worker");
		                        	entry.getData(new ASCIIWriter(), function(text) {
			                          	view.@geogebra.html5.util.View::putIntoArciveContent(Ljava/lang/String;Ljava/lang/String;)(filename,decodeUTF8(text));
			                         });
		                        }
		                        	
		                	}
	            	})(entries[i]);
	            } 
	            reader.close();
	        });
	    },
	    function (error) {
	    	@geogebra.common.main.App::error(Ljava/lang/String;)(error);
	    });
    }-*/;

	public void processFileName(String url) {
		archiveContent = new HashMap<String, String>();
		String workerUrls = (!Browser.webWorkerSupported ? "false" : GWT.getModuleBaseURL()+"js/zipjs/");
	    populateArchiveContentFromFile(url, workerUrls, this);
    }

	private native void populateArchiveContentFromFile(String url, String workerUrls,
            View view) /*-{
		// Writer for ASCII strings
				function ASCIIWriter() {
					var that = this, data;
				
					function init(callback, onerror) {
						data = "";
						callback();
					}
				
					function writeUint8Array(array, callback, onerror) {
						var i;
						for (i = 0; i < array.length; i++)
							data += $wnd.String.fromCharCode(array[i]);
						callback();
					}
					
					function getData(callback) {		
						callback(data);
					}
				
					that.init = init;
					that.writeUint8Array = writeUint8Array;
					that.getData = getData;
				}
				ASCIIWriter.prototype = new $wnd.zip.Writer();
				ASCIIWriter.prototype.constructor = ASCIIWriter;
				
				function decodeUTF8(str_data) {
					var tmp_arr = [], i = 0, ac = 0, c1 = 0, c2 = 0, c3 = 0;
				
					str_data += '';
				
					while (i < str_data.length) {
						c1 = str_data.charCodeAt(i);
						if (c1 < 128) {
							tmp_arr[ac++] = String.fromCharCode(c1);
							i++;
						} else if (c1 > 191 && c1 < 224) {
							c2 = str_data.charCodeAt(i + 1);
							tmp_arr[ac++] = String.fromCharCode(((c1 & 31) << 6) | (c2 & 63));
							i += 2;
						} else {
							c2 = str_data.charCodeAt(i + 1);
							c3 = str_data.charCodeAt(i + 2);
							tmp_arr[ac++] = String.fromCharCode(((c1 & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
							i += 3;
						}
					}
			
					return tmp_arr.join('');
				}		
				
			    var imageRegex = /\.(png|jpg|jpeg|gif|bmp)$/i;
			    if (workerUrls === "false") {
			    	$wnd.zip.useWebWorkers = false;
			    } else {
		    		$wnd.zip.workerScriptsPath = workerUrls;
			    }
			    
			    $wnd.zip.createReader(new $wnd.zip.HttpReader(url),function(reader) {
			        reader.getEntries(function(entries) {
			        	view.@geogebra.html5.util.View::zippedLength = entries.length;
			            for (var i = 0, l = entries.length; i < l; i++) {
			            	(function(entry){
				            	var filename = entry.filename;
				                if (entry.filename.match(imageRegex)) {
				                        @geogebra.common.main.App::debug(Ljava/lang/String;)(filename+" : image");
				                        entry.getData(new $wnd.zip.Data64URIWriter("image/"+filename.split(".")[1]), function (data) {
				                            view.@geogebra.html5.util.View::putIntoArciveContent(Ljava/lang/String;Ljava/lang/String;)(filename,data);
				                        });
				                    } else {
				                        @geogebra.common.main.App::debug(Ljava/lang/String;)(entry.filename+" : text");
				                        if ($wnd.zip.useWebWorkers === false || (typeof $wnd.zip.forceDataURIWriter !== "undefined" && $wnd.zip.forceDataURIWriter === true)) {
				                            @geogebra.common.main.App::debug(Ljava/lang/String;)("no worker of forced dataURIWriter");
					                        entry.getData(new $wnd.zip.Data64URIWriter("text/plain"), function(data) {
					                			var decoded = $wnd.atob(data.substr(data.indexOf(",")+1));
					                          	view.@geogebra.html5.util.View::putIntoArciveContent(Ljava/lang/String;Ljava/lang/String;)(filename,decodeUTF8(decoded));
					                         });
				                        } else {
				                        	@geogebra.common.main.App::debug(Ljava/lang/String;)("worker");
				                        	entry.getData(new ASCIIWriter(), function(text) {
					                          	view.@geogebra.html5.util.View::putIntoArciveContent(Ljava/lang/String;Ljava/lang/String;)(filename,decodeUTF8(text));
					                         });
				                        }
				                        	
				                	}
			            	})(entries[i]);
			            } 
			            reader.close();
			        });
			    },
			    function (error) {
			    	@geogebra.common.main.App::error(Ljava/lang/String;)(error);
			    });
    }-*/;




}
