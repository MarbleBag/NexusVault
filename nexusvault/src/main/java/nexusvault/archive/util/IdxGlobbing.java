package nexusvault.archive.util;

import java.util.List;

import nexusvault.archive.IdxEntry;
import nexusvault.archive.IdxFileLink;

class IdxGlobbing implements IdxEntryVisitor, IdxCollector<IdxEntry> {

	// TODO

	private final String glob;

	public IdxGlobbing(String glob) {
		if (glob == null) {
			throw new IllegalArgumentException();
		}
		this.glob = glob;
	}

	@Override
	public EntryFilterResult visitFile(IdxFileLink file) {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IdxEntry> getAndClearResult() {
		// TODO Auto-generated method stub
		return null;
	}

}
