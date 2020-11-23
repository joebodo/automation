import de.hybris.platform.util.CSVConstants
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource

def content = '''
	$targetType=LogFile
	REMOVE $targetType[batchmode=true];itemtype(code)[unique=true]
	;$targetType
'''

def mediaRes = new StreamBasedImpExResource(new ByteArrayInputStream(content.bytes), CSVConstants.HYBRIS_ENCODING)
importService.importData(mediaRes)

return 'done'
